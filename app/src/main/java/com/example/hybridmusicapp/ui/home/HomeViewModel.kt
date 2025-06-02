package com.example.hybridmusicapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.hybridmusicapp.ResultCallback
import com.example.hybridmusicapp.data.model.album.Album
import com.example.hybridmusicapp.data.model.album.AlbumSongCrossRef
import com.example.hybridmusicapp.data.model.song.Song
import com.example.hybridmusicapp.data.repository.album.AlbumRepository
import com.example.hybridmusicapp.data.repository.album.AlbumRepositoryImp
import com.example.hybridmusicapp.data.repository.song.SongRepository
import com.example.hybridmusicapp.data.repository.song.SongRepositoryImp
import com.example.hybridmusicapp.data.source.remote.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.collections.MutableList

class HomeViewModel(
    private val albumRepository: AlbumRepositoryImp,
    private val songRepository: SongRepositoryImp,
) : ViewModel() {
    private var dispatcher: CoroutineDispatcher = Dispatchers.IO

    private val _remoteSongs: MutableList<Song> = mutableListOf()
    val remoteSongs = _remoteSongs

    private val _remoteSongLoaded = MutableLiveData<Boolean>()
    val remoteSongLoaded: LiveData<Boolean>
        get() = _remoteSongLoaded

    private val _albums = MutableLiveData<List<Album>?>()
    val albums: LiveData<List<Album>?>
        get() = _albums

    private val _topReplaySong = MutableLiveData<List<Song>>()
    val topReplaySong: LiveData<List<Song>> = _topReplaySong

    private val _localSongs = MutableLiveData<List<Song>?>()
    val localSongs: LiveData<List<Song>?>
        get() = _localSongs

    fun loadRemoteSongs() {
        viewModelScope.launch(dispatcher) {
            val result = songRepository.loadRemoteSongs()
            if (result is Result.Success) {
                _remoteSongs.clear()
                _remoteSongs.addAll(result.data.songs)
                _remoteSongLoaded.postValue(true)
            } else if (result is Result.Failure) {
                // TODO: HomeViewModel -> Handle exception
                _remoteSongLoaded.postValue(false)
            }
        }
    }

    fun addSongToFireStore(songs: List<Song>) {
        viewModelScope.launch(dispatcher) {
            songRepository.addSongToFireStore(songs)
        }
    }

    fun getTop10MostHeard() {
        viewModelScope.launch(dispatcher) {
            songRepository.getTop10MostHeard(object : ResultCallback<Result<List<Song>>> {
                override fun onResult(result: Result<List<Song>>) {
                    if (result is Result.Success) {
                        _remoteSongs.clear()
                        _remoteSongs.addAll(result.data)
                        _remoteSongLoaded.postValue(true)
                    } else if (result is Result.Failure) {
                        _remoteSongLoaded.postValue(false)
                        _remoteSongs.addAll(emptyList())
                    }
                }
            })
        }
    }

    fun getTop15Replay() {
        viewModelScope.launch(dispatcher) {
            songRepository.getTop15Replay(object : ResultCallback<Result<List<Song>>> {
                override fun onResult(result: Result<List<Song>>) {
                    if (result is Result.Success) {
                        _topReplaySong.postValue(result.data)
                    } else if (result is Result.Failure) {
                        _topReplaySong.postValue(emptyList())
                    }
                }
            })
        }
    }

    fun reloadData() {
        loadRemoteSongs()
        getTop10MostHeard()
    }

    fun loadLocalSongs() {
        viewModelScope.launch(dispatcher) {
            val result = songRepository.getSongList()
            _localSongs.postValue(result)
        }
    }

    fun saveSongToDB(callback: ResultCallback<Boolean>) {
        viewModelScope.launch(dispatcher) {
            val songs = extractSongs()
            if (songs.isNotEmpty()) {
                val songArr = songs.toTypedArray()
                val result = songRepository.insert(*songArr)
                callback.onResult(result)
            }
        }
    }

    fun saveAlbumToDB(albums: List<Album>, callback: ResultCallback<Boolean>) {
        if (albums.isNotEmpty()) {
            viewModelScope.launch(dispatcher) {
                val albumArr = albums.toTypedArray()
                val result = albumRepository.saveAlbums(*albumArr)
                callback.onResult(result)
            }
        }
    }

    fun saveAlbumSongCrossRef(albums: List<Album>) {
        if (albums.isNotEmpty()) {
            viewModelScope.launch(dispatcher) {
                val crossRefs = createAlbumSongCrossRef(albums)
                if(crossRefs.isNotEmpty()){
                    albumRepository.saveAlbumCrossRef(*crossRefs)
                }
            }
        }
    }

    private fun createAlbumSongCrossRef(albums: List<Album>): Array<AlbumSongCrossRef> {
        val crossRefs: MutableList<AlbumSongCrossRef> = ArrayList()
        for (album in albums) {
            for (songId in album.songs!!) {
                crossRefs.add(AlbumSongCrossRef(album.id, songId))
            }
        }
        return crossRefs.toTypedArray<AlbumSongCrossRef>()
    }

    private fun extractSongs(): List<Song> {
        val result: MutableList<Song> = ArrayList()
        val localSongs = _localSongs.value
        if (localSongs == null) {
            result.addAll(_remoteSongs)
        } else {                                  // localSongs != null
            for (song in _remoteSongs) {
                if (!localSongs.contains(song)) { // Song is not in local
                    result.add(song)
                }
            }
        }
        return result
    }

    class Factory(
        private val songRepository: SongRepositoryImp,
        private val albumRepository: AlbumRepositoryImp
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
                return HomeViewModel(albumRepository, songRepository) as T
            } else {
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }
}