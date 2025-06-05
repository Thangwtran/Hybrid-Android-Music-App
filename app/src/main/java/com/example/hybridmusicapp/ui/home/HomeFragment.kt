package com.example.hybridmusicapp.ui.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import com.example.hybridmusicapp.HomeActivity
import com.example.hybridmusicapp.MusicApplication
import com.example.hybridmusicapp.PlayerBaseFragment
import com.example.hybridmusicapp.R
import com.example.hybridmusicapp.ResultCallback
import com.example.hybridmusicapp.data.model.album.Album
import com.example.hybridmusicapp.data.model.artist.Artist
import com.example.hybridmusicapp.data.model.playing_song.PlayingSong
import com.example.hybridmusicapp.data.model.playlist.Playlist
import com.example.hybridmusicapp.data.model.song.NCSong
import com.example.hybridmusicapp.data.model.song.Song
import com.example.hybridmusicapp.databinding.FragmentHomeBinding
import com.example.hybridmusicapp.ui.home.album.CarouselAdapter
import com.example.hybridmusicapp.ui.home.adapter.RecommendedSong
import com.example.hybridmusicapp.ui.home.adapter.RecommendedSongAdapter
import com.example.hybridmusicapp.ui.home.artist.TopArtistAdapter
import com.example.hybridmusicapp.ui.home.adapter.TrendingNcsTrackAdapter
import com.example.hybridmusicapp.ui.home.album.AlbumFragment
import com.example.hybridmusicapp.ui.home.album.AlbumViewModel
import com.example.hybridmusicapp.ui.home.artist.ArtistDetailFragment
import com.example.hybridmusicapp.ui.viewmodel.ArtistViewModel
import com.example.hybridmusicapp.ui.now_playing.MiniPlayerViewModel
import com.example.hybridmusicapp.ui.viewmodel.MediaViewModel
import com.example.hybridmusicapp.ui.viewmodel.NcsViewModel
import com.example.hybridmusicapp.ui.viewmodel.NowPlayingViewModel
import com.example.hybridmusicapp.utils.MusicAppUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.getValue

class HomeFragment : PlayerBaseFragment() {
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
    private val ncsViewModel by activityViewModels<NcsViewModel> {
        val application = requireActivity().application as MusicApplication
        val ncsRepository = application.ncsRepository
        NcsViewModel.Factory(ncsRepository)
    }
//    private val miniPlayerViewModel by activityViewModels<MiniPlayerViewModel> {
//        val application = requireActivity().application as MusicApplication
//        val songRepository = application.songRepository
//        MiniPlayerViewModel.Factory(songRepository)
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ncsViewModel.getNCSongs()
        albumViewModel.getTop10AlbumsFireStore()
        homeViewModel.getTop10MostHeard()
        homeViewModel.getTop15Replay()
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

        ncsViewModel.ncsSongs.observe(viewLifecycleOwner) { ncsSongs ->
            if (ncsSongs.isNotEmpty()) {
                trendingNcsTrackAdapter.updateNcsTracks(ncsSongs)
            } else {
                Toast.makeText(requireContext(), "No songs found", Toast.LENGTH_SHORT).show()
            }
        }
        trendingNcsTrackAdapter =
            TrendingNcsTrackAdapter(object : TrendingNcsTrackAdapter.OnNcsItemClickListener {
                override fun onNcsSongClick(
                    ncs: NCSong,
                    index: Int
                ) {
                    playNcsSong(ncs, index)
                    Toast.makeText(requireContext(), "$index", Toast.LENGTH_SHORT).show()
                }
            })
        binding.rvNcsTracks.adapter = trendingNcsTrackAdapter
    }

    private fun playNcsSong(
        ncSong: NCSong,
        songIndex: Int
    ) {
        miniPlayerViewModel.setPlayingState(true)
        val playlistName = MusicAppUtils.DefaultPlaylistName.NCS_SONG.value
        NowPlayingViewModel.instance?.setNcsIsPlaying(true)
        setupPlayer(song = null, ncSong, songIndex, playlistName)
    }

    private fun setupTopArtist() {
        artistViewModel.getTop20Artists()
        topArtistAdapter = TopArtistAdapter(object : TopArtistAdapter.OnItemArtistClickListener {
            override fun onItemClick(artist: Artist) {
                artistViewModel.setArtist(artist)
                Toast.makeText(requireContext(), artist.name, Toast.LENGTH_SHORT).show()

                replaceFragment(ArtistDetailFragment())
            }

        })
        artistViewModel.remoteArtist.observe(viewLifecycleOwner) { artists ->
            if (artists != null) {
                topArtistAdapter.updateArtists(artists)
            } else {
                Toast.makeText(requireContext(), "Load Artist Error", Toast.LENGTH_LONG).show()
            }
        }
        binding.rvArtist.adapter = topArtistAdapter
    }

    private fun setUpRecommendedSong() {
        recommendedSongAdapter =
            RecommendedSongAdapter(object : RecommendedSongAdapter.OnRecommendItemClickListener {
                override fun onItemClick(
                    recommendedSong: RecommendedSong,
                    index: Int
                ) {
                    val song = Song(
                        title = recommendedSong.title,
                        artist = recommendedSong.artist,
                        image = recommendedSong.remoteImageRes,
                        source = recommendedSong.remoteAudioUrl
                    )
                    val ncsSong = NCSong(
                        ncsName = recommendedSong.title,
                        artist = recommendedSong.artist,
                        imageRes = recommendedSong.imageRes,
                        audioRes = recommendedSong.ncsAudioRes
                    )
                    val mediaController = MediaViewModel.instance.mediaController.value
                    val playingSong = PlayingSong()
                    if(ncsSong.audioRes != 0){
                        playingSong.ncSong = ncsSong
                        playingSong.isNcsSong = true
                        playingSong.setNcsMediaItem(ncsSong,requireContext())
                        mediaController?.clearMediaItems()
                        mediaController?.addMediaItem(playingSong.mediaItem!!)
                        mediaController?.prepare()
                        mediaController?.play()
                    }else{
                        playingSong.song = song
                        playingSong.isNcsSong = false
                    }
                    Log.i("HomeFragment", "playingSong: ${playingSong.song?.title}")
                    Log.i("HomeFragment", "playingSongNcs: ${playingSong.ncSong?.ncsName}")
                    NowPlayingViewModel.instance?.setPlayingSong(playingSong)
                    NowPlayingViewModel.instance?.setCurrentPlaylist(playingSong.playlist)
//                    NowPlayingViewModel.instance?.setIndexToPlay(index)
                    NowPlayingViewModel.instance?.setMiniPlayerVisible(true)
//                    NowPlayingViewModel.instance?.setIndexToPlay(null)
                    mediaController?.clearMediaItems()
                    mediaController?.addMediaItem(playingSong.mediaItem!!)
                    mediaController?.prepare()
                    mediaController?.play()
                }

            })
        binding.rvRecommended.adapter = recommendedSongAdapter

        val listRecommendedSong = mutableListOf<RecommendedSong>()
        val recommendGenre =
            requireActivity().intent.getStringArrayListExtra(HomeActivity.EXTRA_GENRE_RECOMMENDED) as List<String>
        if (recommendGenre.isNotEmpty()) {
            // remote
            homeViewModel.remoteSongLoaded.observe(viewLifecycleOwner) { isLoaded ->
                if (isLoaded) {
                    val songList = homeViewModel.topReplaySong.value
                    if(songList != null){
                        for (genre in recommendGenre) {
                            if (genre == "Vietnamese Pop") {
                                for (song in songList) {
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
                    }else{
                        Toast.makeText(requireContext(), "No songs found", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "No songs found", Toast.LENGTH_SHORT)
                        .show()
                }

                // local
                ncsViewModel.ncsSongs.observe(viewLifecycleOwner) {
                    for (song in it) {
                        val ncsGenre = song.genre.trim().split('/')
                        Log.i("HomeFragment", "ncs: $ncsGenre")
                        for (genre in recommendGenre) {
                            if (ncsGenre.size > 1) {
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
                            } else {
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

                recommendedSongAdapter.updateRecommendedSongs(listRecommendedSong)

            }
        } else { // recommend genre is empty
            if (MusicAppUtils.isNetworkAvailable(requireContext())) {
                homeViewModel.topReplaySong.observe(viewLifecycleOwner) { replaySongs ->
                    for (song in replaySongs) {
                        listRecommendedSong.add(
                            RecommendedSong(
                                remoteImageRes = song.image,
                                title = song.title,
                                remoteAudioUrl = song.source,
                                artist = song.artist
                            )
                        )
                    }
                    recommendedSongAdapter.updateRecommendedSongs(listRecommendedSong)
                }
            } else { // no internet
                ncsViewModel.ncsSongs.observe(viewLifecycleOwner) { ncsSongs ->
                    for (song in ncsSongs.reversed()) {
                        listRecommendedSong.add(
                            RecommendedSong(
                                imageRes = song.imageRes,
                                title = song.ncsName,
                                ncsAudioRes = song.audioRes,
                                artist = song.artist
                            )
                        )
                    }
                    recommendedSongAdapter.updateRecommendedSongs(listRecommendedSong)
                }
            }
        }
    }

    private fun setupCarousel() {
        albumViewModel.albums.observe(viewLifecycleOwner) { albums ->
            if (albums != null) {
//                saveAlbumToDB(albums)
                albumViewModel.saveAlbumsToDB(albums)
                albumViewModel.saveAlbumSongCrossRef(albums)
                carouselAdapter.updateAlbums(albums)
            } else {
                Toast.makeText(requireContext(), "Error loading albums", Toast.LENGTH_SHORT).show()
            }

        }
        carouselAdapter = CarouselAdapter(object : CarouselAdapter.OnAlbumClickListener {
            override fun onClick(album: Album) {
                Toast.makeText(requireContext(), album.name, Toast.LENGTH_SHORT).show()
                albumViewModel.setAlbum(album)
                replaceFragment(AlbumFragment())
            }
        })
        binding.rvCarousel.adapter = carouselAdapter
    }

    private fun saveAlbumToDB(albums: List<Album>) {
        homeViewModel.saveAlbumToDB(albums, object : ResultCallback<Boolean> {
            override fun onResult(result: Boolean) {
                if (result) {
                    homeViewModel.saveAlbumSongCrossRef(albums)
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Save Album Cross Ref Error",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

        })
    }


    private fun replaceFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(fragment.toString())
            .setReorderingAllowed(true)
            .commit()
    }


}