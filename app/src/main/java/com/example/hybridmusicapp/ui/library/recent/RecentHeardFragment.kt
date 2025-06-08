package com.example.hybridmusicapp.ui.library.recent

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hybridmusicapp.MusicApplication
import com.example.hybridmusicapp.PlayerBaseFragment
import com.example.hybridmusicapp.R
import com.example.hybridmusicapp.data.model.playing_song.PlayingSong
import com.example.hybridmusicapp.data.model.song.NCSong
import com.example.hybridmusicapp.data.model.song.Song
import com.example.hybridmusicapp.databinding.FragmentRecentHeardBinding
import com.example.hybridmusicapp.ui.home.album.SongListAdapter
import com.example.hybridmusicapp.ui.searching.SearchingViewModel
import com.example.hybridmusicapp.ui.viewmodel.MediaViewModel
import com.example.hybridmusicapp.ui.viewmodel.NowPlayingViewModel
import com.example.hybridmusicapp.utils.MusicAppUtils
import kotlin.getValue
import kotlin.text.toInt

class RecentHeardFragment : PlayerBaseFragment() {
    private lateinit var binding: FragmentRecentHeardBinding
    private val recentSongViewModel by activityViewModels<RecentSongViewModel>()
    private val searchingViewModel by activityViewModels<SearchingViewModel>() {
        val application = requireActivity().application as MusicApplication
        SearchingViewModel.Factory(application.searchingRepository)
    }
    private lateinit var recentSongAdapter: RecentSongsAdapter
    private lateinit var recentNCSAdapter: RecentNcsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRecentHeardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupObserve()
    }

    private fun setupView() {
        recentSongAdapter = RecentSongsAdapter(
            object : SongListAdapter.OnItemClickListener {
                override fun onItemClick(
                    song: Song,
                    index: Int
                ) {
                    playingSong(song, index)
                }
            },
            object : SongListAdapter.OnMenuItemClick {
                override fun onMenuItemClick(song: Song) {
                    TODO("Not yet implemented")
                }

            })

        recentNCSAdapter = RecentNcsAdapter(object : RecentNcsAdapter.OnItemClickListener {
            override fun onItemClick(
                song: NCSong,
                index: Int
            ) {
                playNcsSong(song, index)
//                playNcsSingle(song,index)
            }
        }, object : RecentNcsAdapter.OnMenuItemClick {
            override fun onMenuItemClick(ncs: NCSong) {
                TODO("Not yet implemented")
            }

        })

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
        binding.rvRecentHeardNcs.layoutManager = layoutManager2
        binding.rvRecentHeard.layoutManager = layoutManager

        binding.textRecentHeard.setOnClickListener {
            // TODO: show more 
        }
        binding.rvRecentHeard.adapter = recentSongAdapter
        binding.rvRecentHeardNcs.adapter = recentNCSAdapter

    }

    private fun setupObserve() {
        recentSongViewModel.recentSong.observe(viewLifecycleOwner) {
            recentSongAdapter.updateSongs(it)
        }

        recentSongViewModel.recentNcsSong.observe(viewLifecycleOwner) {
            recentNCSAdapter.updateSongs(it)
        }

        NowPlayingViewModel.instance?.playingSong?.observe(viewLifecycleOwner) {
            val isNcs = it.isNcsSong
            if (isNcs) {
                recentNCSAdapter.updateCurrentPlayingIndex(0)
                recentSongAdapter.updateCurrentPlayingIndex(-1)
            } else {
                recentNCSAdapter.updateCurrentPlayingIndex(-1)
                recentSongAdapter.updateCurrentPlayingIndex(0)
            }
        }

    }

    private fun playingSong(song: Song, index: Int) {
        val isInternetAccess = MusicAppUtils.isNetworkAvailable(requireContext())
        if (isInternetAccess) {
            val playlist = MusicAppUtils.DefaultPlaylistName.RECENT.value
            miniPlayerViewModel.setPlayingState(true)
            NowPlayingViewModel.instance?.setNcsIsPlaying(false)
            setupPlayer(song, null, index, playlist)
        } else {
            // TODO: Handle no internet in album
        }
    }


    private fun playNcsSong(
        ncSong: NCSong,
        songIndex: Int
    ) {
        miniPlayerViewModel.setPlayingState(true)
        val playlistName = MusicAppUtils.DefaultPlaylistName.RECENT_NCS.value
        NowPlayingViewModel.instance?.setNcsIsPlaying(true)
        setupPlayer(song = null, ncSong, songIndex, playlistName)
    }


    internal class RecentSongsAdapter(
        listener: SongListAdapter.OnItemClickListener,
        menuItemClick: SongListAdapter.OnMenuItemClick
    ) : SongListAdapter(listener, menuItemClick)


    /**
     * Lớp tùy biến GridLayoutManager theo ý của ta. Cụ thể trong lớp này
     * ta sẽ thay đổi chiều rộng của tất cả các item của RecyclerView.
     */
    internal class MyLayoutManager(
        context: Context?, spanCount: Int,
        orientation: Int, reverseLayout: Boolean
    ) : GridLayoutManager(context, spanCount, orientation, reverseLayout) {
        override fun checkLayoutParams(lp: RecyclerView.LayoutParams): Boolean {
            val dx = (64 * MusicAppUtils.DENSITY).toInt() // giảm một lượng 48dp
            lp.width = width - dx // cập nhật lại chiều rộng của item view
            return true
        }
    }
}