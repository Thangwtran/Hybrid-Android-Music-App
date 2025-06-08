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
import com.example.hybridmusicapp.data.model.song.NCSong
import com.example.hybridmusicapp.data.model.song.Song
import com.example.hybridmusicapp.ui.viewmodel.MediaViewModel
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

    private lateinit var mediaSession: MediaSession

    private val nowPlayingViewModel = NowPlayingViewModel.instance

    private lateinit var listener: Player.Listener



    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {
        return mediaSession;
    }

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

    @OptIn(UnstableApi::class)
    private fun setupPlayerListener() {
        val player = mediaSession.player as ExoPlayer

        listener = object : Player.Listener {
            @OptIn(UnstableApi::class)
            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                val playlistChanged = reason == Player.TIMELINE_CHANGE_REASON_PLAYLIST_CHANGED
                val indexToPlay = nowPlayingViewModel?.indexToPlay?.value
                Log.i("PlaybackService", "index: $indexToPlay, $reason") // now playing viewmodel null
                /**
                 * Chỉ cập nhật nếu không phải thay đổi danh sách phát hoặc indexToPlay là 0.
                 * Điều này tránh cập nhật không cần thiết khi danh sách phát thay đổi.
                 */

                if (!playlistChanged || indexToPlay != null && indexToPlay == 0) {
                    Log.i("PlaybackService", "here")
                    Log.i("PlaybackService", "${player.currentMediaItemIndex}")
                    nowPlayingViewModel?.setPlayingSongIndex(player.currentMediaItemIndex)
                    saveDataToDB()
                }

                if (MusicAppUtils.sConfigChanged) {
                    MusicAppUtils.sConfigChanged = false
                }
            }

            override fun onAudioSessionIdChanged(audioSessionId: Int) {
                super.onAudioSessionIdChanged(audioSessionId)
                MediaViewModel.instance.setAudioSession(audioSessionId)
                Log.i("PlaybackService", "audioSessionId: $audioSessionId")
            }

        }
        player.addListener(listener)

    }


    private fun saveDataToDB() {
        val song = extractSong()
        val ncs = extractNcs()
        if(song != null) {
            val handler = Looper.myLooper()?.let {
                Handler(it)
            }
            handler?.postDelayed({
                val player = mediaSession.player
                if(player.isPlaying){
                    nowPlayingViewModel?.insertRecentSongToDB(song)
//                    saveReplayInfoToDB()
                }
            },2000)
        }
        if(ncs != null) {
            val handler = Looper.myLooper()?.let {
                Handler(it)
            }
            handler?.postDelayed({
                val player = mediaSession.player
                if(player.isPlaying){
                    nowPlayingViewModel?.insertRecentNcsToDB(ncs)
//                    saveReplayInfoToDB()
                }
            },2000)
        }
    }

    private fun extractNcs(): NCSong? {
        return nowPlayingViewModel?.playingSong?.value?.ncSong
    }

    private fun extractSong(): Song? {
        return nowPlayingViewModel?.playingSong?.value?.song
    }

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



    /**
     * Khởi tạo ExoPlayer và MediaSession.
     * Liên kết player với MediaSession và thiết lập PendingIntent để mở PlayerActivity.
     */
    @OptIn(UnstableApi::class)
    private fun initSessionAndPlayer() {
        /**
         * Tạo ExoPlayer với cấu hình AudioAttributes mặc định.
         * - AudioAttributes.DEFAULT: Sử dụng thuộc tính âm thanh cơ bản, phù hợp cho phát nhạc.
         * - handleAudioFocus = true: Tự động quản lý focus âm thanh (tạm dừng khi có cuộc gọi, v.v.).
         */
        val player = ExoPlayer.Builder(this)
            .setAudioAttributes(AudioAttributes.DEFAULT, true)
            .build()
        val audioSessionId = player.audioSessionId
        MediaViewModel.instance.setAudioSession(audioSessionId)

        Log.i("PlaybackService", "audioSessionInit: $audioSessionId")
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