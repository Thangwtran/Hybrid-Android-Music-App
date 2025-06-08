package com.example.hybridmusicapp.ui.home.album

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.example.hybridmusicapp.MusicApplication
import com.example.hybridmusicapp.PlayerBaseFragment
import com.example.hybridmusicapp.R
import com.example.hybridmusicapp.data.model.album.Album
import com.example.hybridmusicapp.data.model.song.Song
import com.example.hybridmusicapp.databinding.FragmentAlbumBinding
import com.example.hybridmusicapp.ui.viewmodel.MediaViewModel
import com.example.hybridmusicapp.ui.viewmodel.NowPlayingViewModel
import com.example.hybridmusicapp.utils.MusicAppUtils
import kotlinx.coroutines.launch

class AlbumFragment : PlayerBaseFragment() {
    private lateinit var binding: FragmentAlbumBinding
    private lateinit var songListAdapter: SongListAdapter

    private val albumViewModel by activityViewModels<AlbumViewModel> {
        val application = requireActivity().application as MusicApplication
        AlbumViewModel.Factory(application.albumRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAlbumBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolBar()
        setupViews()
        setupObserver()
    }

    private fun setupViews() {
        songListAdapter = SongListAdapter(
            object : SongListAdapter.OnItemClickListener {
                override fun onItemClick(song: Song, index: Int) {
                    playingSong(song, index)
                }
            },
            object : SongListAdapter.OnMenuItemClick {
                override fun onMenuItemClick(song: Song) {
                    TODO("Menu click")
                }
            }
        )
        binding.rvAlbum.adapter = songListAdapter

    }

    private fun setupToolBar() {
        binding.abToolbar.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupObserver() {
        albumViewModel.album.observe(viewLifecycleOwner) { album ->
            showAlbum(album)
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    albumViewModel.getAlbumWithSongs(album.id).collect { albumWithSongs ->
                        songListAdapter.updateSongs(albumWithSongs?.songs)
                        if (albumWithSongs != null) {
                            albumViewModel.setPlaylistSongs(albumWithSongs)
                            val playlist = albumViewModel.playlist
                            Log.i("AlbumFragment", "Playlist: $albumWithSongs")
                            NowPlayingViewModel.instance?.addPlaylist(playlist)
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "No album songs found",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
        NowPlayingViewModel.instance?.indexToPlay?.observe(viewLifecycleOwner){
            songListAdapter.updateCurrentPlayingIndex(it)
        }
    }

    private fun playingSong(song: Song, index: Int) {
        val isInternetAccess = MusicAppUtils.isNetworkAvailable(requireContext())
        val audioSessionId = MediaViewModel.instance.audioSession.value
        Log.i("AlbumFragment", "audioSessionId: $audioSessionId")
        if (isInternetAccess) {
            val playlist = albumViewModel.playlist
            NowPlayingViewModel.instance?.playlistName = playlist.name
            miniPlayerViewModel.setPlayingState(true)
            NowPlayingViewModel.instance?.setNcsIsPlaying(false)
            setupPlayer(song, null, index, playlist.name)
        } else {
            // TODO: Handle no internet in album
        }
    }

    private fun showAlbum(album: Album) {
        Glide.with(binding.abImg)
            .load(album.artwork)
            .error(R.drawable.itunes)
            .into(binding.abImg)
        binding.abToolbar.title = album.name
        binding.textAlbumName.text = album.name
    }

}