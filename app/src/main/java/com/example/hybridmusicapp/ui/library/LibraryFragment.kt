package com.example.hybridmusicapp.ui.library

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.hybridmusicapp.MusicApplication
import com.example.hybridmusicapp.R
import com.example.hybridmusicapp.databinding.FragmentLibraryBinding
import com.example.hybridmusicapp.ui.library.favourite.FavouriteSongViewModel
import com.example.hybridmusicapp.ui.library.recent.RecentSongViewModel
import com.example.hybridmusicapp.ui.searching.SearchingFragment
import com.example.hybridmusicapp.ui.viewmodel.NowPlayingViewModel
import com.example.hybridmusicapp.utils.MusicAppUtils


class LibraryFragment : Fragment() {
    private lateinit var binding: FragmentLibraryBinding
    private val nowPlayingViewModel = NowPlayingViewModel.instance
    private val recentSongViewModel by activityViewModels<RecentSongViewModel>()
    private val favouriteSongViewModel by activityViewModels<FavouriteSongViewModel>()
    private val libraryViewModel by activityViewModels<LibraryViewModel>() {
        val application = requireActivity().application as MusicApplication
        LibraryViewModel.Factory(application.songRepository,application.ncsRepository, application.recentSongRepository)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLibraryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setupObserve()
    }

    private fun setupToolbar() {
        binding.icSearch.setOnClickListener {
            replaceFragment(SearchingFragment())
        }
    }

    private fun setupObserve() {
        // favourite
        libraryViewModel.favouriteSongs.observe(viewLifecycleOwner) { favouriteSongs ->
            nowPlayingViewModel?.setupPlaylist(
                favouriteSongs,
                MusicAppUtils.DefaultPlaylistName.FAVORITE.value
            )
            favouriteSongViewModel.setFavouriteSongs(favouriteSongs)
        }

        libraryViewModel.favouriteNcsSongs.observe(viewLifecycleOwner) { favouriteNcsSongs ->
            nowPlayingViewModel?.setupNcsPlaylist(
                requireContext(),
                favouriteNcsSongs,
                MusicAppUtils.DefaultPlaylistName.FAVORITE_NCS.value
            )
            favouriteSongViewModel.setFavouriteNcs(favouriteNcsSongs)
        }

        // recent songs
        libraryViewModel.recentSongs.observe(viewLifecycleOwner) { recentSongs ->
            if (recentSongs.isNotEmpty()) {
                if (recentSongs.size > 10) {
                    nowPlayingViewModel?.setupPlaylist(
                        recentSongs.subList(0, 10),
                        MusicAppUtils.DefaultPlaylistName.RECENT.value
                    )
                } else {
                    nowPlayingViewModel?.setupPlaylist(
                        recentSongs,
                        MusicAppUtils.DefaultPlaylistName.RECENT.value
                    )
                }
            }
            recentSongViewModel.setRecentSong(recentSongs)
        }
        libraryViewModel.recentNcsSongs.observe(viewLifecycleOwner) {
            recentSongViewModel.setRecentNcs(it)
            nowPlayingViewModel?.setupNcsPlaylist(
                requireContext(),
                it,
                MusicAppUtils.DefaultPlaylistName.RECENT_NCS.value
            )
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(fragment.toString())
            .setReorderingAllowed(true)
            .commit()
    }
}