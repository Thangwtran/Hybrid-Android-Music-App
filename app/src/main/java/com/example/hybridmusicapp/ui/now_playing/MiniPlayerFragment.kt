package com.example.hybridmusicapp.ui.now_playing

import android.animation.Animator
import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import com.example.hybridmusicapp.MusicApplication
import com.example.hybridmusicapp.data.model.playlist.Playlist
import com.example.hybridmusicapp.databinding.FragmentMiniPlayerBinding
import com.example.hybridmusicapp.ui.viewmodel.MediaViewModel
import com.example.hybridmusicapp.ui.viewmodel.NowPlayingViewModel
import com.example.hybridmusicapp.utils.MusicAppUtils
import com.example.hybridmusicapp.utils.MusicAppUtils.DefaultPlaylistName


class MiniPlayerFragment : Fragment() {
    private lateinit var binding: FragmentMiniPlayerBinding

    private var mediaController: MediaController? = null

    private lateinit var rotationAnimator: ObjectAnimator

    private lateinit var btnPressedAnimator: Animator

    private lateinit var listener: Player.Listener

    private var playlistName: String = ""

    private var isRegistered = false

    private var nowPlayingViewModel: NowPlayingViewModel? = null

    private val miniPlayerViewModel by activityViewModels<MiniPlayerViewModel> {
        val application = requireActivity().application as MusicApplication
        val songRepository = application.songRepository
        MiniPlayerViewModel.Factory(songRepository)
    }

//    private val playlistViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMiniPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMediaController()
    }

    private fun setupMediaController() {
        MediaViewModel.instance
            .mediaController
            .observe(viewLifecycleOwner) { controller ->
                mediaController = controller
                registerMediaController()
            }
    }

    private fun registerMediaController() {
        // 1. Tạo một Player.Listener để theo dõi sự kiện phát lại
        listener = object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                miniPlayerViewModel.setPlayingState(isPlaying)
            }
        }
        // 2. Kiểm tra nếu mediaController tồn tại
        if (mediaController != null) {
            mediaController?.addListener(listener)
            // 3. Quan sát danh sách MediaItem từ miniPlayerViewModel
            miniPlayerViewModel.mediaItems.observe(viewLifecycleOwner) { items ->
                if (MusicAppUtils.sConfigChanged) { // Nếu cấu hình thay đổi (ví dụ: xoay màn hình), bỏ qua
                    return@observe
                }
                val playlist = nowPlayingViewModel?.currentPlaylist?.value
                // 4. Kiểm tra điều kiện để cập nhật danh sách phát
                if (playlist != null &&
                    (mediaController?.mediaItemCount == 0 || // Danh sách phát trống
                            playlistName.compareTo(playlist.name) != 0 || // Danh sách phát thay đổi
                            playlist.name.compareTo(DefaultPlaylistName.SEARCHED.value) == 0 || // Danh sách tìm kiếm
                            isCustomPlaylist(playlist)) // Danh sách phát tùy chỉnh
                ){
                    // Đánh dấu danh sách phát đã thay đổi
                    miniPlayerViewModel.isPlaylistChanged = true
                    // Cập nhật danh sách MediaItem vào mediaController
                    mediaController?.setMediaItems(items)
                    // Lưu tên danh sách phát hiện tại
                    playlistName = playlist.name
                    // 5. Nếu chưa đăng ký, theo dõi chỉ số bài hát để phát
                    if (!isRegistered) {
                        setIndexToPlay()
                        isRegistered = true
                    }
                }

            }
        } else {
            Log.e("MiniPlayerFragment", "MediaController is null")
        }
    }

    private fun setIndexToPlay() {
        TODO("Not yet implemented")
    }

    private fun isCustomPlaylist(playlist: Playlist): Boolean {
        TODO("Not yet implemented")
    }
}


