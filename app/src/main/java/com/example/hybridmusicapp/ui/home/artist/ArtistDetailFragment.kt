package com.example.hybridmusicapp.ui.home.artist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.example.hybridmusicapp.MusicApplication
import com.example.hybridmusicapp.PlayerBaseFragment
import com.example.hybridmusicapp.R
import com.example.hybridmusicapp.data.model.album.Album
import com.example.hybridmusicapp.data.model.artist.Artist
import com.example.hybridmusicapp.data.model.song.Song
import com.example.hybridmusicapp.databinding.FragmentAlbumBinding
import com.example.hybridmusicapp.databinding.FragmentArtistDetailBinding
import com.example.hybridmusicapp.ui.home.album.AlbumViewModel
import com.example.hybridmusicapp.ui.home.album.SongListAdapter
import com.example.hybridmusicapp.ui.viewmodel.ArtistViewModel
import com.example.hybridmusicapp.ui.viewmodel.MediaViewModel
import com.example.hybridmusicapp.ui.viewmodel.NowPlayingViewModel
import com.example.hybridmusicapp.utils.MusicAppUtils
import kotlinx.coroutines.launch


class ArtistDetailFragment : PlayerBaseFragment() {
    private lateinit var binding: FragmentArtistDetailBinding
    private lateinit var songListAdapter: SongListAdapter

//    private val artistViewModel by activityViewModels<ArtistViewModel> {
//        val application = requireActivity().application as MusicApplication
//        ArtistViewModel.Factory(application.artistRepository)
//    }
    private val artistViewModel by activityViewModels<ArtistViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentArtistDetailBinding.inflate(inflater, container, false)
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
        binding.artistToolbar.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupObserver() {
        artistViewModel.artist.observe(viewLifecycleOwner) { artist ->
            showArtist(artist)
            artistViewModel.getArtistWithSongs(artist.id)
            artistViewModel.artistWithSongs.observe(viewLifecycleOwner) { artistWithSongs ->
                songListAdapter.updateSongs(artistWithSongs.songs)
                if (artistWithSongs != null) {
                    artistViewModel.setPlaylistSongs(artistWithSongs)
                    val playlist = artistViewModel.playlist
                    NowPlayingViewModel.instance?.addPlaylist(playlist)
                } else {
                    Toast.makeText(requireContext(), "No artist songs found", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            NowPlayingViewModel.instance?.indexToPlay?.observe(viewLifecycleOwner){
                songListAdapter.updateCurrentPlayingIndex(it)
            }
        }
    }

    private fun playingSong(song: Song, index: Int) {
        val isInternetAccess = MusicAppUtils.isNetworkAvailable(requireContext())
        if (isInternetAccess) {
            val playlist = artistViewModel.playlist
            NowPlayingViewModel.instance?.playlistName = playlist.name
            miniPlayerViewModel.setPlayingState(true)
            NowPlayingViewModel.instance?.setNcsIsPlaying(false)
            setupPlayer(song, null, index, playlist.name)
        } else {
            Toast.makeText(requireContext(), "No internet", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showArtist(artist: Artist) {
        Glide.with(binding.artistImg)
            .load(artist.avatar)
            .error(R.drawable.itunes)
            .into(binding.artistImg)
        binding.artistToolbar.title = artist.name
    }
}