package com.example.hybridmusicapp.ui.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.hybridmusicapp.MusicApplication
import com.example.hybridmusicapp.data.model.album.Album
import com.example.hybridmusicapp.databinding.FragmentHomeBinding
import com.example.hybridmusicapp.ui.home.adapter.CarouselAdapter
import com.example.hybridmusicapp.ui.home.adapter.RecommendedSong
import com.example.hybridmusicapp.ui.home.adapter.RecommendedSongAdapter
import com.example.hybridmusicapp.ui.home.adapter.TopArtistAdapter
import com.example.hybridmusicapp.ui.home.adapter.TrendingNcsTrackAdapter
import com.example.hybridmusicapp.ui.home.AlbumViewModel
import com.example.hybridmusicapp.ui.home.ArtistViewModel
import com.example.hybridmusicapp.ui.viewmodel.NcsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.getValue

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var carouselAdapter: CarouselAdapter
    private lateinit var recommendedSongAdapter: RecommendedSongAdapter
    private lateinit var topArtistAdapter: TopArtistAdapter
    private lateinit var trendingNcsTrackAdapter: TrendingNcsTrackAdapter

    private val albumViewModel by activityViewModels<AlbumViewModel> {
        val application = requireActivity().application as MusicApplication
        AlbumViewModel.Factory(application.albumRepository)
    }
    private val artistViewModel by activityViewModels<ArtistViewModel> {
        val application = requireActivity().application as MusicApplication
        val artistRepository = application.artistRepository
        ArtistViewModel.Factory(artistRepository)
    }
    private val homeViewModel by activityViewModels<HomeViewModel> {
        val application = requireActivity().application as MusicApplication
        val songRepository = application.songRepository
        val albumRepository = application.albumRepository
        HomeViewModel.Factory(songRepository, albumRepository)
    }
    private val ncsViewModel by activityViewModels<NcsViewModel> {
        val application = requireActivity().application as MusicApplication
        val ncsRepository = application.ncsRepository
        NcsViewModel.Factory(ncsRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ncsViewModel.getNCSongs()
        albumViewModel.getTop10AlbumsFireStore()
        homeViewModel.getTop10MostHeard()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCarousel()
        setUpRecommendedSong()
        setupTopArtist()
        setupTrendingNcsTrack()
    }

    private fun setupTrendingNcsTrack() {
        trendingNcsTrackAdapter = TrendingNcsTrackAdapter()
        ncsViewModel.ncsSongs.observe (viewLifecycleOwner){ ncsSongs ->
            if(ncsSongs.isNotEmpty()){
                trendingNcsTrackAdapter.updateNcsTracks(ncsSongs)
            }else{
                Toast.makeText(requireContext(), "No songs found", Toast.LENGTH_SHORT).show()
            }
        }
        binding.rvNcsTracks.adapter = trendingNcsTrackAdapter
    }

    private fun setupTopArtist() {
        artistViewModel.getTop20Artists()
        topArtistAdapter = TopArtistAdapter()
        artistViewModel.remoteArtists.observe(viewLifecycleOwner) { artists ->
            if (artists != null) {
                topArtistAdapter.updateArtists(artists)
            } else {
                Toast.makeText(requireContext(), "Load Artist Error", Toast.LENGTH_LONG).show()
            }
        }
        binding.rvArtist.adapter = topArtistAdapter
    }

    private fun setUpRecommendedSong() {
        val listRecommendedSong = mutableListOf<RecommendedSong>()
        lifecycleScope.launch(Dispatchers.Main) {
            val recommendGenre =
                requireActivity().intent.getStringArrayListExtra(HomeActivity.EXTRA_GENRE_RECOMMENDED) as List<String>
            Log.d("HomeFragment", "setUpRecommendedSong: $recommendGenre")

            // remote
//            homeViewModel.getTop10MostHeard()
            homeViewModel.remoteSongs.observe(viewLifecycleOwner) { remoteSongs ->
                if (remoteSongs != null) {
                    for (genre in recommendGenre) {
                        if (genre == "Vietnamese Pop") {
                            for (song in remoteSongs) {
                                listRecommendedSong.add(
                                    RecommendedSong(
                                        remoteImageRes = song.image,
                                        title = song.title,
                                        remoteAudioUrl = song.source,
                                        artist = song.artist
                                    )
                                )
                            }
                        }

                    }
                } else {
                    Toast.makeText(requireContext(), "No songs found", Toast.LENGTH_SHORT).show()
                }

                // local
//                ncsViewModel.getNCSongs()
                ncsViewModel.ncsSongs.observe(viewLifecycleOwner) {
                    for (song in it) {
                        val ncsGenre = song.genre.trim().split('/')
                        Log.i("HomeFragment", "ncs: $ncsGenre")
                        for (genre in recommendGenre) {
                            if(ncsGenre.size > 1){
                                if (genre == ncsGenre[0].trim() || genre == ncsGenre[1].trim()) {
                                    listRecommendedSong.add(
                                        RecommendedSong(
                                            imageRes = song.imageRes,
                                            title = song.ncsName,
                                            ncsAudioRes = song.audioRes,
                                            artist = song.artist
                                        )
                                    )
                                }
                            }else{
                                if (genre == ncsGenre[0].trim()) {
                                    listRecommendedSong.add(
                                        RecommendedSong(
                                            imageRes = song.imageRes,
                                            title = song.ncsName,
                                            ncsAudioRes = song.audioRes,
                                            artist = song.artist
                                        )
                                    )
                                }
                            }

                        }
                    }
                }

                //adapter
                recommendedSongAdapter = RecommendedSongAdapter()
                recommendedSongAdapter.updateRecommendedSongs(listRecommendedSong)
                binding.rvRecommended.adapter = recommendedSongAdapter
            }


        }

    }

    private fun setupCarousel() {
        carouselAdapter = CarouselAdapter(object : CarouselAdapter.OnAlbumClickListener {
            override fun onClick(album: Album) {
                Toast.makeText(requireContext(), album.name, Toast.LENGTH_SHORT).show()
            }
        })

        albumViewModel.albums.observe(viewLifecycleOwner) {
            if (it != null) {
                carouselAdapter.updateAlbums(it)
            } else {
                Toast.makeText(requireContext(), "Error loading albums", Toast.LENGTH_SHORT).show()
            }

        }
        binding.rvCarousel.adapter = carouselAdapter

    }


}