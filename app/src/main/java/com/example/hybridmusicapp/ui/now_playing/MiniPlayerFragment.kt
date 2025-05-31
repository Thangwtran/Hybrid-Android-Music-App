package com.example.hybridmusicapp.ui.now_playing

import android.animation.Animator
import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.TranslateAnimation
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import com.bumptech.glide.Glide
import com.example.hybridmusicapp.MusicApplication
import com.example.hybridmusicapp.PlayerBaseFragment
import com.example.hybridmusicapp.R
import com.example.hybridmusicapp.data.model.playing_song.PlayingSong
import com.example.hybridmusicapp.data.model.playlist.Playlist
import com.example.hybridmusicapp.data.model.song.NCSong
import com.example.hybridmusicapp.data.model.song.Song
import com.example.hybridmusicapp.databinding.FragmentMiniPlayerBinding
import com.example.hybridmusicapp.ui.viewmodel.MediaViewModel
import com.example.hybridmusicapp.ui.viewmodel.NcsViewModel
import com.example.hybridmusicapp.ui.viewmodel.NowPlayingViewModel
import com.example.hybridmusicapp.ui.viewmodel.PlaylistViewModel
import com.example.hybridmusicapp.utils.MusicAppUtils
import com.example.hybridmusicapp.utils.MusicAppUtils.DefaultPlaylistName

class MiniPlayerFragment : PlayerBaseFragment(), View.OnClickListener {
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

    private val playlistViewModel by activityViewModels<PlaylistViewModel> {
        val application = requireActivity().application as MusicApplication
        PlaylistViewModel.Factory(application.playlistRepository)
    }

    private val ncsViewModel by activityViewModels<NcsViewModel> {
        val application = requireActivity().application as MusicApplication
        val ncsRepository = application.ncsRepository
        NcsViewModel.Factory(ncsRepository)
    }


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
        // TODO: update song status

    }

    override fun onDestroy() {
        super.onDestroy()
        mediaController?.removeListener(listener)
    }

    private fun observeData() {
        nowPlayingViewModel = NowPlayingViewModel.instance

        // ncs playlist
        ncsViewModel.getNCSongs()
        ncsViewModel.ncsSongs.observe(viewLifecycleOwner) { ncsSongList ->
            nowPlayingViewModel?.setupNcsPlaylist(
                requireContext(),
                ncsSongList,
                DefaultPlaylistName.NCS_SONG.value
            )
            playlistViewModel.setNcsPlaylist(ncsSongs = ncsSongList)
        }


        nowPlayingViewModel?.playingSong?.observe(viewLifecycleOwner) { playingSong ->
            mediaController?.prepare()
            mediaController?.play()
            showSongMiniPlayerUI(playingSong)
        }

        // set media items
        nowPlayingViewModel?.currentPlaylist?.observe(viewLifecycleOwner) { currentPlaylist ->
            if (!MusicAppUtils.sConfigChanged) {
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
        Log.i("MiniPlayerFragment", "showSongMiniPlayerUI: ${playingSong.ncSong}")

        if (playingSong.ncSong == null) {
            binding.miniPlayerSongName.text = playingSong.song?.title
            binding.miniPlayerArtist.text = playingSong.song?.artist
            Glide.with(requireContext())
                .load(playingSong.song?.image)
                .error(R.drawable.itunes)
                .into(binding.imgMiniPlayerArtwork)

            updateFavouriteStatus(playingSong.song, ncSong = null)
        } else if (playingSong.song == null) {
            binding.miniPlayerSongName.text = playingSong.ncSong?.ncsName
            binding.miniPlayerSongName.isSelected = true
            binding.miniPlayerArtist.text = playingSong.ncSong?.artist
            Glide.with(requireContext())
                .load(playingSong.ncSong?.imageRes)
                .error(R.drawable.itunes)
                .into(binding.imgMiniPlayerArtwork)

            updateFavouriteStatus(song = null, playingSong.ncSong)
        } else {
            Toast.makeText(requireContext(), "Song data is null", Toast.LENGTH_SHORT).show()
        }

    }

    private fun setupListeners() {
        binding.btnMiniPlayerPlayPause.setOnClickListener(this)
        binding.btnMiniPlayerFavourite.setOnClickListener(this)
        binding.btnMiniPlayerNext.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        if (view == binding.btnMiniPlayerPlayPause) { // play pause
            Toast.makeText(requireContext(), "play pause", Toast.LENGTH_SHORT).show()
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

    private fun updateFavouriteStatus(song: Song?, ncSong: NCSong?) {
        if (ncSong == null) {
            if (song!!.isFavorite) {
                binding.btnMiniPlayerFavourite.setImageResource(androidx.media3.session.R.drawable.media3_icon_heart_filled)
            } else {
                binding.btnMiniPlayerFavourite.setImageResource(androidx.media3.session.R.drawable.media3_icon_heart_unfilled)
            }
        } else if (song == null) {
            if (ncSong!!.isFavourite) {
                binding.btnMiniPlayerFavourite.setImageResource(androidx.media3.session.R.drawable.media3_icon_heart_filled)
            } else {
                binding.btnMiniPlayerFavourite.setImageResource(androidx.media3.session.R.drawable.media3_icon_heart_unfilled)
            }
        }

    }

    private fun setupMediaController() {
        MediaViewModel.instance
            .mediaController
            .observe(viewLifecycleOwner) { controller ->
                mediaController = controller
                registerMediaController()
            }
    }

    /**
     * Đăng ký listener và xử lý danh sách MediaItem cho MediaController.
     * - Tạo Player.Listener để theo dõi sự kiện phát lại (isPlayingChanged).
     * - Quan sát danh sách MediaItem từ miniPlayerViewModel để cập nhật danh sách phát.
     * - Kiểm tra các điều kiện để quyết định cập nhật danh sách MediaItem.
     */
    private fun registerMediaController() {
        listener = object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                miniPlayerViewModel.setPlayingState(isPlaying)
            }
        }
        if (mediaController != null) {
            mediaController?.addListener(listener)

            miniPlayerViewModel.mediaItems.observe(viewLifecycleOwner) { items ->

                if (MusicAppUtils.sConfigChanged) {
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
                if (playlist != null // &&
//                    (mediaController?.mediaItemCount == 0 ||
//                            playlistName.compareTo(playlist.name) != 0 ||
//                            playlist.name.compareTo(DefaultPlaylistName.SEARCHED.value) == 0 ||
//                            isCustomPlaylist(playlist))
                ) {
                    miniPlayerViewModel.isPlaylistChanged = true
                    mediaController?.setMediaItems(items)
                    playlistName = playlist.name
                    if (!isRegistered) {
                        setIndexToPlay()
                        isRegistered = true
                    }
                } else {
                    Log.d("MiniPlayerFragment", "Playlist is empty")
                    Toast.makeText(requireContext(), "Playlist is empty", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Log.i("MiniPlayerFragment", "MediaController is null")
        }
    }

    /**
     * Kiểm tra điều kiện để phát bài hát tại chỉ số index:
     *  - index khác -1 (giá trị hợp lệ, vì -1 thường biểu thị không có bài hát được chọn)
     *  - Và một trong hai điều kiện sau:
     *  1. Chỉ số hiện tại của mediaController khác index và danh sách bài hát có đủ bài (mediaItemCount > index)
     *  2. Chỉ số hiện tại bằng index nhưng danh sách phát đã thay đổi (isPlaylistChanged = true)
     */
    private fun setIndexToPlay() {
        nowPlayingViewModel?.indexToPlay?.observe(viewLifecycleOwner) { index ->
            if (MusicAppUtils.sConfigChanged || index == -1) {
                return@observe
            }

            /**
             * Kiểm tra nếu chỉ số hợp lệ và cần phát bài hát tại chỉ số này.
             * Nếu đúng, gọi hàm playMediaAtIndex để phát bài hát.
             * Ngược lại, nếu chỉ số vượt quá số lượng bài hát, chờ danh sách phát thay đổi.
             */
            if (isValidIndex(index) && shouldPlayIndex(index)) {
                playMediaAtIndex(index)
            } else if (index >= mediaController?.mediaItemCount!!) {
                waitForPlaylistChanged(index)
            }
        }
    }

    /**
     * Kiểm tra xem chỉ số bài hát có hợp lệ hay không.
     * Chỉ số được coi là hợp lệ nếu nó nhỏ hơn số lượng bài hát trong danh sách phát.
     * @param index Chỉ số bài hát cần kiểm tra.
     * @return True nếu chỉ số hợp lệ, false nếu không.
     */
    private fun isValidIndex(index: Int): Boolean {
        return mediaController?.mediaItemCount!! > index
    }

    /**
     * Kiểm tra xem có nên phát bài hát tại chỉ số được chỉ định hay không.
     * Phát bài nếu chỉ số hiện tại khác với chỉ số cần phát hoặc danh sách phát đã thay đổi.
     * @param index Chỉ số bài hát cần kiểm tra.
     * @return True nếu nên phát bài hát, false nếu không.
     */
    private fun shouldPlayIndex(index: Int): Boolean {
        /**
         * Kiểm tra hai điều kiện:
         * - Chỉ số hiện tại của mediaController khác với index (cần chuyển bài hát).
         * - Hoặc chỉ số hiện tại bằng index nhưng danh sách phát đã thay đổi (cần làm mới).
         */
        return (mediaController?.currentMediaItemIndex != index && miniPlayerViewModel.isPlaylistChanged)
    }

    private fun playMediaAtIndex(index: Int) {
        // Di chuyển đến bài hát tại vị trí index, bắt đầu từ thời điểm 0 (đầu bài hát).
        mediaController?.seekTo(index, 0)
        mediaController?.prepare()
        miniPlayerViewModel.isPlaylistChanged = false
    }


    /**
     * Chờ danh sách phát thay đổi để thử phát bài hát tại chỉ số được chỉ định.
     * Thêm một listener để theo dõi sự kiện thay đổi danh sách phát và phát bài hát khi cần.
     * @param index Chỉ số bài hát cần phát.
     */
    private fun waitForPlaylistChanged(index: Int) {
        mediaController?.addListener(object : Player.Listener {
            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                if (reason == Player.MEDIA_ITEM_TRANSITION_REASON_PLAYLIST_CHANGED) {
                    playMediaAtIndex(index)
                    mediaController?.removeListener(listener)
                }
            }
        })
    }

    private fun isCustomPlaylist(playlist: Playlist): Boolean {
        val playlists = playlistViewModel.playlists.value // list playlist
        if (playlists != null) {
            for ((playlist1) in playlists) {
                if (playlist1!!.name.compareTo(playlist.name) == 0) {
                    return true
                }
            }
        }
        return false
    }

    private fun setupSkipNext() {
        nowPlayingViewModel?.indexToPlay?.observe(viewLifecycleOwner) { index ->
            if (MusicAppUtils.sConfigChanged) {
                return@observe
            }
            if (index != -1 && ((mediaController?.currentMediaItemIndex != index
                        && mediaController?.mediaItemCount!! > index)
                        || (mediaController?.currentMediaItemIndex == index
                        && miniPlayerViewModel.isPlaylistChanged))
            ) {
                mediaController?.seekTo(index, 0)
                mediaController?.prepare()
                miniPlayerViewModel.isPlaylistChanged = false
            } else if (mediaController?.mediaItemCount!! <= index) {
                mediaController?.addListener(object : Player.Listener {
                    override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                        if (reason == Player.MEDIA_ITEM_TRANSITION_REASON_PLAYLIST_CHANGED) {
                            mediaController?.seekTo(index, 0)
                            mediaController?.prepare()
                            miniPlayerViewModel.isPlaylistChanged = false
                        }
                    }
                })
            }
        }
    }

    private fun setupFavourite() { // favourite click
        val playingSong = nowPlayingViewModel?.playingSong?.value
        Log.i("MiniPlayerFragment", "setupFavourite: $playingSong")
        if (playingSong != null) {
            val song = playingSong.song
            val ncsSong = playingSong.ncSong
            Log.i("MiniPlayerFragment", "setupFavourite: $ncsSong")

            if (song != null) {
                song.isFavorite = !song.isFavorite
                nowPlayingViewModel?.updateFavouriteStatus(song)
                updateFavouriteStatus(song, ncsSong)
            }else if (ncsSong != null) {
                ncsSong.isFavourite = !ncsSong.isFavourite
                nowPlayingViewModel?.updateNcsFavouriteStatus(ncsSong)
                updateFavouriteStatus(song, ncsSong)
            }else{
                Toast.makeText(requireContext(), "song, ncs is null", Toast.LENGTH_SHORT).show()
            }

        }
    }
}


