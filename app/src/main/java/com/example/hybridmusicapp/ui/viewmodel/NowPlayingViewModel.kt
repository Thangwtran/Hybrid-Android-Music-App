package com.example.hybridmusicapp.ui.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.hybridmusicapp.data.model.playing_song.PlayingSong
import com.example.hybridmusicapp.data.model.playlist.Playlist
import com.example.hybridmusicapp.data.model.playlist.PlaylistWithSongs
import com.example.hybridmusicapp.data.model.song.NCSong
import com.example.hybridmusicapp.data.model.song.Song
import com.example.hybridmusicapp.data.repository.recent_song.RecentSongRepositoryImp
import com.example.hybridmusicapp.data.repository.search.SearchingRepositoryImp
import com.example.hybridmusicapp.data.repository.song.NCSongRepositoryImp
import com.example.hybridmusicapp.data.repository.song.SongRepositoryImp
import com.example.hybridmusicapp.utils.MusicAppUtils.DefaultPlaylistName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.concurrent.Volatile

class NowPlayingViewModel private constructor(
    songRepository: SongRepositoryImp,
    ncsSongRepository: NCSongRepositoryImp,
    searchRepository: SearchingRepositoryImp,
    recentSongRepository: RecentSongRepositoryImp
) : ViewModel() {

    // Repository

    private var _songRepository: SongRepositoryImp = songRepository
    private var _ncsSongRepository: NCSongRepositoryImp = ncsSongRepository
    private val _searchingRepository: SearchingRepositoryImp = searchRepository
    private val _recentSongRepository: RecentSongRepositoryImp = recentSongRepository

    // Playing Song
    private val _playingSong = PlayingSong()
    private val _playingSongLiveData = MutableLiveData<PlayingSong>()
    val playingSong: LiveData<PlayingSong>
        get() = _playingSongLiveData

    // Playlist
    private var _playlistName: String = "" // Is ncs after set
    var playlistName: String
        get() = playlistName
        set(playlistName) {
            _playlistName = playlistName
            val playlist = getPlaylist(playlistName) // get default playlist
            setCurrentPlaylist(playlist)
        }
    private val _currentPlaylist = MutableLiveData<Playlist?>()
    val currentPlaylist: LiveData<Playlist?> = _currentPlaylist
    private val _playlistMap: MutableMap<String, Playlist?> = HashMap()

    // Index
    private val _indexToPlay = MutableLiveData<Int>()   // is nulling
    val indexToPlay: LiveData<Int> = _indexToPlay

    private val _miniPlayerVisibility = MutableLiveData<Boolean>()
    val isMiniPlayerVisible: LiveData<Boolean> = _miniPlayerVisibility

    val historySearchSongs: LiveData<List<Song>>
        get() = _searchingRepository.historySearchedSongs.asLiveData()

    /**
     * init
     */
    init {
        initPlaylists()
    }

    /**
     * Song Management
     */

    fun setPlayingSongIndex(index: Int) {
        if (index != -1
            && (_playingSong.playlist?.songs?.size ?: 0) > index
            || (_playingSong.playlist?.ncsSongs?.size ?: 0) > index
        ) {
            Log.e("NowPlayingViewModel", "song: ${_playingSong.song}")
            Log.e("NowPlayingViewModel", "ncsSong: ${_playingSong.ncSong}")
            if (_playingSong.playlist!!.songs.isNotEmpty()) {
                val song = _playingSong.playlist!!.songs[index]
                _playingSong.apply {
                    this.song = song
                    currentPosition = 0
                    currentSongIndex = index
                }
            }
            if(_playingSong.playlist!!.ncsSongs.isNotEmpty()) {
                val ncSong = _playingSong.playlist!!.ncsSongs[index]
                _playingSong.apply {
                    this.ncSong = ncSong
                    currentPosition = 0
                    currentSongIndex = index
                }
            }
            updatePlayingSong()
        }
    }

    fun setNcsIsPlaying(isPlaying: Boolean) {
        _playingSong.isNcsSong = isPlaying
    }
    private fun updatePlayingSong() {
        _playingSongLiveData.value = _playingSong
    }

    fun setPlayingSong(playingSong: PlayingSong) {
        _playingSongLiveData.value = playingSong
    }

    fun updateFavouriteStatus(song: Song) {
        viewModelScope.launch(Dispatchers.IO) {
            song.isFavorite = true
            _songRepository.update(song)


        }
    }
    fun updateNcsFavouriteStatus( ncs: NCSong) {
        viewModelScope.launch(Dispatchers.IO) {
//            ncs.isFavourite = true
            _ncsSongRepository.update(ncs)
        }
    }

    fun updateSongInDB(song: Song) {
        viewModelScope.launch(Dispatchers.IO) {
            song.counter += 1
            song.replay += 1
            _songRepository.update(song)
        }
    }

    fun updateSongCounter(songId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _songRepository.updateSongCounter(songId)
        }
    }



    fun setIndexToPlay(index: Int) {  // is called in PlayerBaseFragment
        _indexToPlay.value = index
    }

    fun loadPrevSessionPlayingSong(songId: String?, playlistName: String?) {

    }


    /**
     * Playlist Management
     */
    // Init playlist map (all playlists have id = -1 and value is playlist name )
    private fun initPlaylists() { // oke
        for (playlistName in DefaultPlaylistName.entries.toTypedArray()) {
            val playlist = Playlist(_id = -1, name = playlistName.value)
            _playlistMap[playlistName.value] = playlist
        }
    }

    // Get playlist by playlist name from map
    private fun getPlaylist(playlistName: String): Playlist? {
        return _playlistMap.getOrDefault(playlistName, null)
    }

    // Update playlist in map
    private fun updatePlaylist(playlist: Playlist): Boolean {
        return _playlistMap.put(playlist.name, playlist) != null
    }

    // Set songs for song playlist
    fun setupPlaylist(songs: List<Song>?, playlistName: String) {
        if (songs != null) {
            val playlist = getPlaylist(playlistName)
            if (playlist != null) {
                playlist.updateSongList(songs)
                updatePlaylist(playlist)
            }
        }
    }

    // Set songs for ncs playlist
    fun setupNcsPlaylist(context: Context, ncsSongs: List<NCSong>?, playlistName: String) {
        if (ncsSongs != null) {
            val playlist = getPlaylist(playlistName)
            if (playlist != null) {
                playlist.updateNcsSongList(ncsSongs, context)
                updatePlaylist(playlist)
            } else {
                Log.i("NowPlayingViewModel", "setupNcsPlaylist: playlist is null")
            }
        }
    }

    // Set current playlist
    fun setCurrentPlaylist(playlist: Playlist?) {
        _currentPlaylist.value = playlist
        if (_playingSong.playlist == null || _playingSong.playlist !== playlist) { // if not same playlist
            _playingSong.playlist = playlist // set new playlist
        }
    }

    // Set playlist with songs (for album)
    fun setPlaylistWithSongs(playlists: List<PlaylistWithSongs>) {
        for (i in playlists.indices) {
            val playlistWithSongs = playlists[i]
            val playlist = playlistWithSongs.playlist
            playlist?.updateSongList(playlist.songs)
            playlist?.let { addPlaylist(it) }
        }
    }

    fun addPlaylist(playlist: Playlist): Boolean {
        if (!_playlistMap.containsKey(playlist.name)) { // if not exist
            return _playlistMap.put(playlist.name, playlist) != null
        }
        // if exist
        return updatePlaylist(playlist)
    }


    /**
     * Mini Player Management
     */

    fun setMiniPlayerVisible(state: Boolean) {
        _miniPlayerVisibility.value = state
    }

    class Factory(
        private val _songRepository: SongRepositoryImp,
        private val _ncsSongRepository: NCSongRepositoryImp,
        private val _searchingRepository: SearchingRepositoryImp,
        private val _recentSongRepository: RecentSongRepositoryImp
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(NowPlayingViewModel::class.java)) {
                return NowPlayingViewModel(
                    _songRepository,
                    _ncsSongRepository,
                    _searchingRepository,
                    _recentSongRepository
                ) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    companion object {
        @Volatile
        var instance: NowPlayingViewModel? = null

        fun getInstance(
            songRepository: SongRepositoryImp,
            ncsSongRepository: NCSongRepositoryImp,
            searchRepository: SearchingRepositoryImp,
            recentSongRepository: RecentSongRepositoryImp
        ): NowPlayingViewModel {
            return instance ?: synchronized(this) { // sync block
                instance ?: NowPlayingViewModel( // instance or if null -> create new
                    songRepository,
                    ncsSongRepository,
                    searchRepository,
                    recentSongRepository
                )
                    .also { instance = it } // set instance = new instance
            }
        }
    }
}