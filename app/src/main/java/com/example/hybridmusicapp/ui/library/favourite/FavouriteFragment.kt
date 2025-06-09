package com.example.hybridmusicapp.ui.library.favourite

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.hybridmusicapp.PlayerBaseFragment
import com.example.hybridmusicapp.data.model.song.NCSong
import com.example.hybridmusicapp.data.model.song.Song
import com.example.hybridmusicapp.databinding.FragmentFavouriteBinding
import com.example.hybridmusicapp.ui.library.recent.RecentHeardFragment.MyLayoutManager
import com.example.hybridmusicapp.ui.viewmodel.NowPlayingViewModel
import com.example.hybridmusicapp.utils.MusicAppUtils

class FavouriteFragment : PlayerBaseFragment() {
    private lateinit var binding: FragmentFavouriteBinding
    private lateinit var favouriteSongAdapter: FavouriteSongAdapter
    private lateinit var favouriteNcsAdapter: FavouriteNcsAdapter
    private val favouriteViewModel by activityViewModels<FavouriteSongViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavouriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        setupObserver()
    }

    private fun setupObserver() {
        favouriteViewModel.favouriteSongs.observe(viewLifecycleOwner) {
            if(it.isNotEmpty()){
                binding.rvFavouriteSong.visibility = View.VISIBLE
                binding.imgNoSong.visibility = View.INVISIBLE
            }else{
                binding.rvFavouriteSong.visibility = View.INVISIBLE
                binding.imgNoSong.visibility = View.VISIBLE
            }
            favouriteSongAdapter.updateSongs(it)
        }

        favouriteViewModel.favouriteNcs.observe(viewLifecycleOwner) {
            if(it.isNotEmpty()){
                binding.rvFavouriteNcs.visibility = View.VISIBLE
                binding.imgNoNcs.visibility = View.GONE
            }else{
                binding.rvFavouriteNcs.visibility = View.INVISIBLE
                binding.imgNoNcs.visibility = View.VISIBLE
            }
            favouriteNcsAdapter.updateSongs(it)
        }

        NowPlayingViewModel.instance?.indexToPlay?.observe(viewLifecycleOwner){
            val isNcs = NowPlayingViewModel.instance?.playingSong?.value?.isNcsSong
            Log.i("FavouriteFragment","isNcs: $isNcs")
            if(isNcs == true || isNcs == null){
                favouriteNcsAdapter.updateCurrentPlayingIndex(it)
                favouriteSongAdapter.updateCurrentPlayingIndex(-1)
            }else{
                favouriteNcsAdapter.updateCurrentPlayingIndex(-1)
                favouriteSongAdapter.updateCurrentPlayingIndex(it)
            }
        }
    }

    private fun setupViews() {
        favouriteSongAdapter = FavouriteSongAdapter(
            object : FavouriteSongAdapter.OnItemClickListener {
                override fun onItemClick(
                    song: Song,
                    index: Int
                ) {
                    playingSong(song,index)
                }
            },
            object : FavouriteSongAdapter.OnMenuItemClick {
                override fun onMenuItemClick(song: Song) {
                    TODO("Not yet implemented")
                }

            }
        )

        favouriteNcsAdapter = FavouriteNcsAdapter(
            object : FavouriteNcsAdapter.OnItemClickListener {
                override fun onItemClick(
                    song: NCSong,
                    index: Int
                ) {
                    playNcsSong(song,index)
                }

            },
            object : FavouriteNcsAdapter.OnMenuItemClick {
                override fun onMenuItemClick(ncs: NCSong) {
                    TODO("Not yet implemented")
                }
            }
        )

        val layoutManager = MyLayoutManager(
            requireContext(),
            3,
            GridLayoutManager.HORIZONTAL,
            false
        )
        val layoutManager2 = MyLayoutManager(
            requireContext(),
            3,
            GridLayoutManager.HORIZONTAL,
            false
        )
        binding.rvFavouriteNcs.layoutManager = layoutManager2
        binding.rvFavouriteSong.layoutManager = layoutManager

        binding.rvFavouriteNcs.adapter = favouriteNcsAdapter
        binding.rvFavouriteSong.adapter = favouriteSongAdapter

    }

    private fun playingSong(song: Song, index: Int) {
        val isInternetAccess = MusicAppUtils.isNetworkAvailable(requireContext())
        if (isInternetAccess) {
            val playlist = MusicAppUtils.DefaultPlaylistName.FAVORITE.value
            miniPlayerViewModel.setPlayingState(true)
            setupPlayer(song, null, index, playlist)
            NowPlayingViewModel.instance?.setNcsIsPlaying(false)

        } else {
            // TODO: Handle no internet in album
        }
    }


    private fun playNcsSong(
        ncSong: NCSong,
        songIndex: Int
    ) {
        miniPlayerViewModel.setPlayingState(true)
        val playlistName = MusicAppUtils.DefaultPlaylistName.FAVORITE_NCS.value
        setupPlayer(song = null, ncSong, songIndex, playlistName)
        NowPlayingViewModel.instance?.setNcsIsPlaying(true)
    }

}