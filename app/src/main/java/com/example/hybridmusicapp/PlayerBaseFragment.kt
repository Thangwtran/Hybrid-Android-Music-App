package com.example.hybridmusicapp

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.hybridmusicapp.data.model.playlist.Playlist
import com.example.hybridmusicapp.data.model.song.NCSong
import com.example.hybridmusicapp.data.model.song.Song
import com.example.hybridmusicapp.ui.home.HomeViewModel
import com.example.hybridmusicapp.ui.now_playing.MiniPlayerViewModel
import com.example.hybridmusicapp.ui.viewmodel.NetworkViewModel
import com.example.hybridmusicapp.ui.viewmodel.NowPlayingViewModel
import com.example.hybridmusicapp.ui.viewmodel.PermissionViewModel

open class PlayerBaseFragment : Fragment() {
    protected val homeViewModel by activityViewModels<HomeViewModel>() {
        val application = requireActivity().application as MusicApplication
        HomeViewModel.Factory(application.songRepository, application.albumRepository)
    }
    protected val miniPlayerViewModel by activityViewModels<MiniPlayerViewModel> {
        val application = requireActivity().application as MusicApplication
        val songRepository = application.songRepository
        MiniPlayerViewModel.Factory(songRepository)
    }

    protected lateinit var networkViewModel: NetworkViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        networkViewModel = NetworkViewModel.instance
    }

    protected fun showServerError() {

    }

    protected fun showNoInternetDialog() {

    }

    protected fun setupPlayer(song: Song?, ncSong: NCSong?, index: Int, playlistName: String) {
        Log.i("PlayerBaseFragment", "setupPlayer: $playlistName, $index") // oke
        val permissionGranted = PermissionViewModel.instance.isPermissionGranted.value
        if (permissionGranted != null && permissionGranted) { // permission granted
            setupPlayerSong(index, playlistName)
        } else if (!PermissionViewModel.isRegistered) {
            PermissionViewModel.instance.isPermissionGranted.observe(viewLifecycleOwner) { permissionGranted ->
                if (permissionGranted) {
                    setupPlayerSong(index, playlistName)
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Permission not granted in PLayerBaseFg",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            // Đánh dấu đã đăng ký để tránh lặp
            PermissionViewModel.isRegistered = true
        }
    }

    protected fun setupPlayerSong(index: Int, playlistName: String) {
        NowPlayingViewModel.instance?.playlistName = playlistName // set current playlist
        NowPlayingViewModel.instance?.setMiniPlayerVisible(true)
        NowPlayingViewModel.instance?.setIndexToPlay(index)
//        Log.i("PlayerBaseFragment", "setupPlayerSong: ${NowPlayingViewModel.instance}")
    }
}