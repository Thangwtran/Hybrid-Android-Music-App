package com.example.hybridmusicapp.ui.now_playing

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.os.Process
import android.widget.Toast
import androidx.annotation.OptIn
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.TaskStackBuilder
import androidx.media3.common.AudioAttributes
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import com.example.hybridmusicapp.R
import com.example.hybridmusicapp.data.model.song.Song
import com.example.hybridmusicapp.ui.viewmodel.NowPlayingViewModel
import com.example.hybridmusicapp.utils.MusicAppUtils


/**
 *  MediaSessionService (1 foreground service) trong Jetpack Media3:
 *  + Quản lý phát lại media (audio) trong background,
 *  + Hiển thị thông báo điều khiển,
 *  + Lưu thông tin bài hát vào Database
 *
 *  Foreground service: đảm bảo phát lại tiếp tục ngay cả khi ứng dụng không ở foreground.
 */
class PlaybackService : MediaSessionService() {

    /**
     * MediaSession: Quản lý phiên media, cho phép điều khiển phát lại và tương tác với hệ thống (notification,...)
     */
    private lateinit var mediaSession: MediaSession

    private val nowPlayingViewModel = NowPlayingViewModel.instance

    private lateinit var listener: Player.Listener


    /**
     * Trả về MediaSession để hệ thống (như Android Auto, Wear OS) hoặc MediaController
     * có thể tương tác với dịch vụ.
     * @param controllerInfo Thông tin về controller yêu cầu MediaSession.
     * @return MediaSession hiện tại, hoặc null nếu không khả dụng.
     */
    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {
        return mediaSession;
    }


    /**
     * Khởi tạo dịch vụ khi được gọi.
     * Khởi tạo ExoPlayer, MediaSession, thiết lập listener cho player,
     * và thiết lập listener cho MediaSessionService để xử lý lỗi foreground.
     */
    @UnstableApi
    override fun onCreate() {
        super.onCreate()
        initSessionAndPlayer()
        setupPlayerListener()
        setListener(MediaSessionServiceListener())
    }


    /**
     * Xử lý khi ứng dụng bị xóa tab (ví dụ: người dùng vuốt bỏ).
     * Dừng dịch vụ nếu player không phát, không có media, hoặc đã kết thúc.
     * Tạm dừng phát lại nếu player đang phát.
     * @param rootIntent Intent gốc của ứng dụng.
     */
    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        val player = mediaSession.player
        if (!player.playWhenReady || player.mediaItemCount == 0 || player.playbackState == Player.STATE_ENDED) {
            stopSelf()
        } else if (player.playWhenReady) {
            player.pause()
        }
    }

    /**
     * Giải phóng tài nguyên khi dịch vụ bị hủy.
     * Cập nhật PendingIntent, xóa listener, giải phóng player và MediaSession.
     */
    @OptIn(UnstableApi::class)
    override fun onDestroy() {
        val pendingIntent = backStackedActivity
        if (pendingIntent != null) {
            mediaSession.setSessionActivity(pendingIntent)
        }
        mediaSession.player.removeListener(listener)
        mediaSession.player.release()
        mediaSession.release()
        clearListener()
        super.onDestroy()
    }

    /**
     * Thiết lập listener cho ExoPlayer để theo dõi sự kiện chuyển đổi bài hát.
     * Cập nhật trạng thái bài hát trong NowPlayingViewModel và lưu vào cơ sở dữ liệu.
     */
    private fun setupPlayerListener() {
        val player = mediaSession.player
        listener = object : Player.Listener {
            /**
             * Được gọi khi chuyển sang MediaItem mới (bài hát mới).
             * Cập nhật trạng thái bài hát trong ViewModel và lưu vào cơ sở dữ liệu.
             * @param mediaItem MediaItem mới, hoặc null nếu không có.
             * @param reason Lý do chuyển đổi (ví dụ: thay đổi danh sách phát).
             */
            @OptIn(UnstableApi::class)
            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                /**
                 * Kiểm tra xem chuyển đổi có do thay đổi danh sách phát hay không.
                 * Sử dụng TIMELINE_CHANGE_REASON_PLAYLIST_CHANGED thay vì
                 * MEDIA_ITEM_TRANSITION_REASON_PLAYLIST_CHANGED
                 * để phù hợp với API mới của Media3.
                 */
                val playlistChanged = reason == Player.TIMELINE_CHANGE_REASON_PLAYLIST_CHANGED
                val indexToPlay = nowPlayingViewModel?.indexToPlay?.value

                Log.i("PlaybackService", "index: $indexToPlay $nowPlayingViewModel") // now playing viewmodel null

                /**
                 * Chỉ cập nhật nếu không phải thay đổi danh sách phát hoặc indexToPlay là 0.
                 * Điều này tránh cập nhật không cần thiết khi danh sách phát thay đổi.
                 */
                if (!playlistChanged || indexToPlay != null && indexToPlay == 0) {
                    nowPlayingViewModel?.setPlayingSongIndex(player.currentMediaItemIndex)
                    saveDataToDB()
                }

                /**
                 * Xử lý thay đổi cấu hình (configuration change) nếu có.
                 * Đặt lại cờ sConfigChanged để tránh xử lý lặp lại.
                 */
                if (MusicAppUtils.sConfigChanged) {
                    MusicAppUtils.sConfigChanged = false
                }
            }

        }
        player.addListener(listener)
    }

    /**
     * Lưu thông tin bài hát đang phát vào cơ sở dữ liệu (bảng RecentSong)
     * sau khi phát 5 giây, nếu player vẫn đang phát.
     */
    private fun saveDataToDB() {
        val song = extractSong()
        if(song != null) {
            val handler = Looper.myLooper()?.let {
                Handler(it)
            }
            handler?.postDelayed({
                val player = mediaSession.player
                if(player.isPlaying){
//                    nowPlayingViewModel?.insertRecentSongToDB(song)
//                    saveReplayInfoToDB()
                }
            },5000)

        }
    }

    /**
     * Cập nhật số lần phát trên remote và local.
     */
    private fun saveReplayInfoToDB(){
        val song = extractSong()
        if(song != null){
            // update on remote
            nowPlayingViewModel?.updateSongCounter(song.id)
            // update on local (On background thread)
            val handlerThread = HandlerThread(
                "UpdateReplayThread",
                Process.THREAD_PRIORITY_BACKGROUND
            )
            handlerThread.start()
            val handler = Handler(handlerThread.looper)
            handler.post {
                nowPlayingViewModel?.updateSongInDB(song)
                /**
                 * Đảm bảo gọi quitSafely để giải phóng HandlerThread sau khi hoàn tất.
                 * Tránh rò rỉ bộ nhớ.
                 */
                handlerThread.quitSafely()
            }
        }
    }

    private fun extractSong(): Song? {
        return nowPlayingViewModel?.playingSong?.value?.song
    }

    /**
     * Khởi tạo ExoPlayer và MediaSession.
     * Liên kết player với MediaSession và thiết lập PendingIntent để mở PlayerActivity.
     */
    private fun initSessionAndPlayer() {
        /**
         * Tạo ExoPlayer với cấu hình AudioAttributes mặc định.
         * - AudioAttributes.DEFAULT: Sử dụng thuộc tính âm thanh cơ bản, phù hợp cho phát nhạc.
         * - handleAudioFocus = true: Tự động quản lý focus âm thanh (tạm dừng khi có cuộc gọi, v.v.).
         */
        val player = ExoPlayer.Builder(this)
            .setAudioAttributes(AudioAttributes.DEFAULT, true)
            .build()

        val builder = MediaSession.Builder(this, player)

        /**
         * Gắn PendingIntent để mở PlayerActivity khi khởi tạo media session.
         */
        val intent = singleTopActivity
        builder.setSessionActivity(intent)
        mediaSession = builder.build()
    }

    /**
     * Tạo PendingIntent để mở PlayerActivity ở chế độ singleTop.
     * Nếu PlayerActivity đã tồn tại ở đỉnh stack, hệ thống sẽ tái sử dụng instance hiện tại
     * thay vì tạo mới, tránh trùng lặp Activity.
     * @return PendingIntent để mở PlayerActivity.
     */
    private val singleTopActivity: PendingIntent
        get() {
            // Sử dụng applicationContext để tránh rò rỉ bộ nhớ từ Activity context.
            val intent = Intent(applicationContext, PlayerActivity::class.java)
            /**
             * Tạo PendingIntent với getActivity để mở PlayerActivity.
             * - this: Context của PlaybackService.
             * - 0: requestCode để xác định PendingIntent (không cần mã cụ thể).
             * - intent: Intent để mở PlayerActivity.
             * - FLAG_IMMUTABLE: Đảm bảo PendingIntent không thể sửa đổi (bảo mật, Android 12+).
             * - FLAG_UPDATE_CURRENT: Cập nhật PendingIntent hiện có nếu đã tồn tại.
             */
            return PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        }

    /**
     * Tạo PendingIntent để mở PlayerActivity với TaskStackBuilder.
     * TaskStackBuilder xây dựng stack điều hướng để tái tạo luồng Activity đúng
     * (ví dụ: MainActivity -> PlayerActivity), đảm bảo trải nghiệm điều hướng mượt mà
     * khi mở từ thông báo hoặc ngoài ứng dụng.
     * @return PendingIntent để mở PlayerActivity, hoặc null nếu có lỗi.
     */
    private val backStackedActivity: PendingIntent?
        get() {
            val taskStackBuilder = TaskStackBuilder.create(this)
            taskStackBuilder.addNextIntent(
                Intent(this@PlaybackService, PlayerActivity::class.java)
            )
            return taskStackBuilder.getPendingIntent(
                0,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        }

    /**
     * Listener để xử lý lỗi khi foreground service không thể khởi động
     * (ví dụ: thiếu quyền POST_NOTIFICATIONS trên Android 13+).
     * Hiển thị Toast và thông báo khi xảy ra lỗi.
     */
    @UnstableApi
    private inner class MediaSessionServiceListener : Listener {
        override fun onForegroundServiceStartNotAllowedException() {
            if (Build.VERSION.SDK_INT >= 33 &&
                checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) !=
                PackageManager.PERMISSION_GRANTED
            ) {
                Toast.makeText(applicationContext, "Permission not granted", Toast.LENGTH_SHORT)
                    .show()
                return
            }

            /**
             * Tạo NotificationManagerCompat để quản lý thông báo.
             */
            val notificationManagerCompat = NotificationManagerCompat.from(this@PlaybackService)
            ensureNotificationChannel(notificationManagerCompat)

            /**
             * Tạo thông báo với biểu tượng, tiêu đề, và nội dung.
             * Sử dụng BigTextStyle để hiển thị nội dung dài.
             */
            val builder = NotificationCompat.Builder(this@PlaybackService, CHANNEL_ID)
                .setSmallIcon(R.drawable.itunes)
                .setContentTitle(getString(R.string.notification_channel_name))
                .setStyle(
                    NotificationCompat.BigTextStyle()
                        .bigText(getString(R.string.notification_content_text))
                )
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
            /**
             * Gắn PendingIntent để mở PlayerActivity khi nhấn thông báo.
             */
            val pendingIntent = backStackedActivity
            if (pendingIntent != null) {
                builder.setContentIntent(pendingIntent)
            }
            notificationManagerCompat.notify(NOTIFICATION_ID, builder.build())
        }
    }

    private fun ensureNotificationChannel(managerCompat: NotificationManagerCompat) {
        if (managerCompat.getNotificationChannel(CHANNEL_ID) != null) {
            return
        }
        val channel = NotificationChannel(
            CHANNEL_ID,
            getString(R.string.notification_channel_name),
            NotificationManager.IMPORTANCE_DEFAULT
        )
        managerCompat.createNotificationChannel(channel)
    }

    companion object {
        const val NOTIFICATION_ID: Int = 9999
        const val CHANNEL_ID: String = "music_app_notification_channel_id"
    }
}