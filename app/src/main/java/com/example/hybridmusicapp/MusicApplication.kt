package com.example.hybridmusicapp

import android.app.Application
import android.content.ComponentName
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.example.hybridmusicapp.data.repository.album.AlbumRepositoryImp
import com.example.hybridmusicapp.data.repository.artist.ArtistRepositoryImp
import com.example.hybridmusicapp.data.repository.playlist.PlaylistRepositoryImp
import com.example.hybridmusicapp.data.repository.recent_song.RecentSongRepositoryImp
import com.example.hybridmusicapp.data.repository.search.SearchingRepositoryImp
import com.example.hybridmusicapp.data.repository.song.NCSongRepositoryImp
import com.example.hybridmusicapp.data.repository.song.SongRepositoryImp
import com.example.hybridmusicapp.ui.now_playing.PlaybackService
import com.example.hybridmusicapp.ui.viewmodel.MediaViewModel
import com.example.hybridmusicapp.utils.InjectionUtils
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import java.util.concurrent.ExecutionException

class MusicApplication : Application() {
    lateinit var searchingRepository: SearchingRepositoryImp
    lateinit var songRepository: SongRepositoryImp
    lateinit var albumRepository: AlbumRepositoryImp
    lateinit var artistRepository: ArtistRepositoryImp
    lateinit var playlistRepository: PlaylistRepositoryImp
    lateinit var recentSongRepository: RecentSongRepositoryImp
    lateinit var ncsRepository: NCSongRepositoryImp

    /**
     * - ListenableFuture là một interface trong thư viện Guava (hoặc Android's ListenableFuture),
     *   đại diện cho một hoạt động bất đồng bộ (asynchronous) có thể trả về kết quả trong tương lai.
     * - ListenableFuture<MediaController> nghĩa là hoạt động bất đồng bộ này sẽ trả về một đối tượng
     *   MediaController khi hoàn thành.
     * - ListenableFuture khác với Future thông thường ở chỗ nó hỗ trợ thêm các callback (listener)
     *   để xử lý kết quả hoặc lỗi ngay khi hoạt động hoàn tất, mà không cần chặn luồng (blocking).
     */
    private lateinit var controllerFuture: ListenableFuture<MediaController>
    private var mediaController: MediaController? = null

    override fun onCreate() {
        super.onCreate()
        setupViewModelComponents()
        createMediaPlayer()
    }

    private fun createMediaPlayer() {

        /**
         * Tạo một SessionToken để thiết lập kết nối giữa ứng dụng và PlaybackService.
         * - SessionToken là một đối tượng trong Android Media Framework, đại diện cho một phiên media
         *   (MediaSession) được cung cấp bởi một dịch vụ phát media (như PlaybackService).
         * - SessionToken hoạt động như một "chìa khóa" để liên kết MediaController với MediaSession,
         *   đảm bảo rằng MediaController chỉ giao tiếp với đúng dịch vụ phát media được chỉ định.
         * - ComponentName: Xác định lớp PlaybackService (dịch vụ phát media) mà MediaController sẽ
         *   kết nối tới. ComponentName bao gồm package name của ứng dụng và tên lớp dịch vụ.
         * - SessionToken đảm bảo giao tiếp an toàn và chính xác giữa MediaController và MediaSession,
         *   hỗ trợ các tính năng như điều khiển từ xa, hiển thị thông tin media trên lock screen, notification
         *   hoặc tương tác với thiết bị ngoại vi (như tai nghe Bluetooth).
         */
        val sessionToken = SessionToken(
            applicationContext,
            ComponentName(applicationContext, PlaybackService::class.java)
        )

        controllerFuture = MediaController.Builder(applicationContext, sessionToken).buildAsync()

        controllerFuture.addListener({
            if (controllerFuture.isDone && !controllerFuture.isCancelled) {
                try {
                    mediaController = controllerFuture.get()
                    mediaController?.let {
                        MediaViewModel.instance.setMediaController(it)
                    }
                } catch (_: ExecutionException) {
                } catch (_: InterruptedException) {
                }
            } else {
                mediaController = null
            }
        }, MoreExecutors.directExecutor()) // thực hiện trực tiếp trên luồng gọi
    }

    private fun setupViewModelComponents() {
        // song
        val localSongDataSource = InjectionUtils.provideLocalDataSource(applicationContext)
        songRepository = InjectionUtils.provideSongRepository(localSongDataSource)

        // recent song
        val recentSongDataSource = InjectionUtils.provideRecentSongDataSource(applicationContext)
        recentSongRepository = InjectionUtils.provideRecentSongRepository(recentSongDataSource)

        // album
        val localAlbumDataSource = InjectionUtils.provideAlbumLocalDataSource(applicationContext)
        albumRepository = InjectionUtils.provideAlbumRepository(localAlbumDataSource)

        // playlist
        val localPlaylistDataSource = InjectionUtils.providePlaylistLocalDataSource(applicationContext)
        playlistRepository = InjectionUtils.providePlaylistRepository(localPlaylistDataSource)

        // artist
        val localArtistDataSource = InjectionUtils.provideArtistLocalDataSource(applicationContext)
        artistRepository = InjectionUtils.provideArtistRepository(localArtistDataSource)

        // searching
        val searchingDataSource = InjectionUtils.provideSearchingDataSource(applicationContext)
        searchingRepository = InjectionUtils.provideSearchingRepository(searchingDataSource)

        // ncs
        val localNCSongDataSource = InjectionUtils.provideNCSongLocalDataSource(applicationContext)
        ncsRepository = InjectionUtils.provideNCSongRepository(localNCSongDataSource)


    }
}