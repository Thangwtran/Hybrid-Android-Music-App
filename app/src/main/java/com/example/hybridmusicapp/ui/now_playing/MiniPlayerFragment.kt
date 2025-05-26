package com.example.hybridmusicapp.ui.now_playing

import android.animation.Animator
import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import com.bumptech.glide.Glide
import com.example.hybridmusicapp.MusicApplication
import com.example.hybridmusicapp.R
import com.example.hybridmusicapp.data.model.playing_song.PlayingSong
import com.example.hybridmusicapp.data.model.playlist.Playlist
import com.example.hybridmusicapp.data.model.song.Song
import com.example.hybridmusicapp.databinding.FragmentMiniPlayerBinding
import com.example.hybridmusicapp.ui.viewmodel.MediaViewModel
import com.example.hybridmusicapp.ui.viewmodel.NowPlayingViewModel
import com.example.hybridmusicapp.utils.MusicAppUtils
import com.example.hybridmusicapp.utils.MusicAppUtils.DefaultPlaylistName

class MiniPlayerFragment : Fragment(), View.OnClickListener {
    private lateinit var binding: FragmentMiniPlayerBinding

    private var mediaController: MediaController? = null

    private lateinit var rotationAnimator: ObjectAnimator

    private lateinit var btnPressedAnimator: Animator

    /**
     * - Player.Listener là một interface của Android Media3, dùng để nhận các sự kiện
     *   như thay đổi trạng thái phát (isPlaying), thay đổi bài hát, v.v.
     * - listener sẽ được khởi tạo trong registerMediaController().
     */
    private lateinit var listener: Player.Listener

    /**
     * Biến playlistName lưu tên của danh sách phát hiện tại.
     * - Dùng để so sánh với danh sách phát mới, xác định xem danh sách có thay đổi hay không.
     * - Khởi tạo rỗng và được cập nhật khi danh sách phát thay đổi.
     */
    private var playlistName: String = ""

    /**
     * Biến isRegistered kiểu Boolean để theo dõi trạng thái đăng ký MediaController.
     * - Đảm bảo rằng chỉ số bài hát (setIndexToPlay) chỉ được thiết lập một lần khi
     *   MediaController được đăng ký lần đầu.
     * - Khởi tạo là false, chuyển thành true sau khi đăng ký.
     */
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
        Log.d("MiniPlayerFragment", "onCreateView")
        binding = FragmentMiniPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMediaController()
        observeData()
        setupListeners()
    }

    override fun onResume() {
        super.onResume()
        // TODO: ONResume
    }

    override fun onDestroy() {
        super.onDestroy()
        // TODO: onDestroy
    }


    /**
     * - Quan sát currentPlayingSong từ nowPlayingViewModel để cập nhật giao diện mini player.
     * - Quan sát currentPlaylist từ nowPlayingViewModel (chưa triển khai logic).
     * - Quan sát isPlaying từ miniPlayerViewModel để cập nhật trạng thái nút play/pause.
     */
    private fun observeData() {
        nowPlayingViewModel = NowPlayingViewModel.instance

        nowPlayingViewModel?.currentPlayingSong?.observe(viewLifecycleOwner) { playingSong ->
            showSongMiniPlayerUI(playingSong)
        }

        nowPlayingViewModel?.currentPlaylist?.observe(viewLifecycleOwner) { currentPlaylist ->
            if(!MusicAppUtils.sConfigChanged){
                currentPlaylist?.mediaItems?.let {
                    miniPlayerViewModel.setMediaItems(it)
                }
            }
        }

        /**
         * Quan sát isPlaying để cập nhật nút play/pause và trạng thái nút next.
         * - Nếu isPlaying = true, hiển thị icon pause và kiểm tra xem nút next có khả dụng không.
         * - Nếu isPlaying = false, hiển thị icon play.
         */
        miniPlayerViewModel.isPlaying.observe(viewLifecycleOwner) { isPlaying ->
            if (isPlaying) {
                binding.btnMiniPlayerPlayPause.setImageResource(androidx.media3.session.R.drawable.media3_icon_pause)
                if (mediaController != null) {
                    binding.btnMiniPlayerNext.isEnabled =
                        (mediaController!!.hasNextMediaItem() || mediaController?.repeatMode == Player.REPEAT_MODE_ALL)
                }
            } else {
                binding.btnMiniPlayerPlayPause.setImageResource(androidx.media3.session.R.drawable.media3_icon_play)
            }
        }
    }

    private fun showSongMiniPlayerUI(playingSong: PlayingSong) {
        binding.miniPlayerSongName.text = playingSong.song?.title
        binding.miniPlayerArtist.text = playingSong.song?.artist
        Glide.with(requireContext())
            .load(playingSong.song?.image)
            .error(R.drawable.itunes)
            .into(binding.imgMiniPlayerArtwork)

        updateFavouriteStatus(playingSong.song)
    }

    private fun setupListeners() {
        binding.btnMiniPlayerPlayPause.setOnClickListener(this)
        binding.btnMiniPlayerFavourite.setOnClickListener(this)
        binding.btnMiniPlayerNext.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        if (view == binding.btnMiniPlayerPlayPause) { // play pause
            if (mediaController!!.isPlaying) {
                mediaController?.pause()
            } else {
                mediaController?.prepare()
                mediaController?.play()
            }
        } else if (view == binding.btnMiniPlayerFavourite) { // favourite
            setupFavourite()
        } else if (view == binding.btnMiniPlayerNext) { // next
            setupSkipNext()
        }
    }

    private fun updateFavouriteStatus(song: Song?) {
        if (song!!.isFavorite) {
            binding.btnMiniPlayerFavourite.setImageResource(R.drawable.ic_heart_selected)
        } else {
            binding.btnMiniPlayerFavourite.setImageResource(R.drawable.ic_heart)
        }
    }

    /**
     * - Khi MediaController được cung cấp, gọi registerMediaController() để đăng ký listener và xử lý danh sách phát.
     */
    private fun setupMediaController() {
        MediaViewModel.instance
            .mediaController
            .observe(viewLifecycleOwner) { controller ->
                mediaController = controller
                registerMediaController()
            }
    }

    /**
     * registerMediaController đăng ký listener và xử lý danh sách MediaItem cho MediaController.
     * - Tạo Player.Listener để theo dõi sự kiện phát lại (isPlayingChanged).
     * - Quan sát danh sách MediaItem từ miniPlayerViewModel để cập nhật danh sách phát.
     * - Kiểm tra các điều kiện để quyết định cập nhật danh sách MediaItem.
     */
    private fun registerMediaController() {
        // Tạo một Player.Listener để theo dõi sự kiện phát lại
        listener = object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                miniPlayerViewModel.setPlayingState(isPlaying)
            }
        }
        if (mediaController != null) {
            Log.d("MiniPlayerFragment", "registerMediaController")

            mediaController?.addListener(listener)

            miniPlayerViewModel.mediaItems.observe(viewLifecycleOwner) { items ->
                if (MusicAppUtils.sConfigChanged) { // Nếu cấu hình thay đổi (ví dụ: xoay màn hình), bỏ qua
                    return@observe
                }
                val playlist = nowPlayingViewModel?.currentPlaylist?.value
                /**
                 *  Kiểm tra điều kiện để cập nhật danh sách phát:
                 * - Danh sách phát trống (mediaItemCount == 0).
                 * - Danh sách phát thay đổi (playlistName khác playlist.name).
                 * - Danh sách phát là danh sách tìm kiếm (SEARCHED).
                 * - Danh sách phát là tùy chỉnh (isCustomPlaylist).
                 */
                if (playlist != null &&
                    (mediaController?.mediaItemCount == 0 ||
                            playlistName.compareTo(playlist.name) != 0 ||
                            playlist.name.compareTo(DefaultPlaylistName.SEARCHED.value) == 0 ||
                            isCustomPlaylist(playlist))
                ) {
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
                }else{
                    Log.d("MiniPlayerFragment", "Playlist is empty")
                    Toast.makeText(requireContext(), "Playlist is empty", Toast.LENGTH_SHORT).show()
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


    private fun setupSkipNext() {
        // TODO:  
    }

    private fun setupFavourite() {
        // TODO:
    }
}


