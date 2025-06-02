package com.example.hybridmusicapp.ui.home.album

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.hybridmusicapp.ResultCallback
import com.example.hybridmusicapp.data.model.album.Album
import com.example.hybridmusicapp.data.model.album.AlbumSongCrossRef
import com.example.hybridmusicapp.data.model.album.AlbumWithSongs
import com.example.hybridmusicapp.data.model.playlist.Playlist
import com.example.hybridmusicapp.data.repository.album.AlbumRepositoryImp
import com.example.hybridmusicapp.data.source.remote.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AlbumViewModel(
    private val albumRepository: AlbumRepositoryImp
) : ViewModel() {
    private val _albums = MutableLiveData<List<Album>?>()
    val albums = _albums

    private val _album = MutableLiveData<Album>()
    val album = _album

    val playlist = Playlist()

    fun setPlaylistSongs(albumWithSongs: AlbumWithSongs){
        val songs = albumWithSongs.songs
        val playlistName = albumWithSongs.album?.name
        playlist.name = playlistName!!
        playlist.updateSongList(songs)
    }


    fun loadAlbums() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = albumRepository.loadRemoteAlbums()
            if (result is Result.Success) {
                _albums.postValue(result.data.playlist)
            } else if (result is Result.Failure) {
                // TODO: Handle exception
            }
        }
    }

    fun addAlbumToFireStore(albums: List<Album>) {
        viewModelScope.launch(Dispatchers.IO) {
            albumRepository.addAlbumToFireStore(albums)
        }
    }

    fun getTop10AlbumsFireStore() {
        viewModelScope.launch(Dispatchers.IO) {
            albumRepository.getTop10AlbumsFireStore(object : ResultCallback<Result<List<Album>>> {
                override fun onResult(result: Result<List<Album>>) {
                    if (result is Result.Success) {
                        _albums.postValue(result.data)
                    } else if (result is Result.Failure) {
                        _albums.postValue(emptyList())
                    }
                }
            })
        }
    }

    fun getAlbumsFireStore() {
        viewModelScope.launch(Dispatchers.IO) {
            albumRepository.getAlbumsFireStore(object : ResultCallback<Result<List<Album>>> {
                override fun onResult(result: Result<List<Album>>) {
                    if (result is Result.Success) {
                        _albums.postValue(result.data)
                    } else if (result is Result.Failure) {
                        _albums.postValue(emptyList())
                    }
                }
            })
        }
    }

    fun saveAlbumsToDB(albums: List<Album>, callback: ResultCallback<Boolean>) {
        if(albums.isNotEmpty()){
            viewModelScope.launch(Dispatchers.IO) {
                val albumArr = albums.toTypedArray()
                val result = albumRepository.saveAlbums(*albumArr)
                callback.onResult(result)
            }
        }
    }

    fun saveAlbumSongCrossRef(albums: List<Album>) {
        if(albums.isNotEmpty()){
            viewModelScope.launch(Dispatchers.IO) {
                val crossRefs = createAlbumSongCrossRef(albums)
                albumRepository.saveAlbumCrossRef(*crossRefs)
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

    fun setAlbum(album: Album) {
        _album.value = album
    }

    fun setAlbums(albums: List<Album>) {
        _albums.value = albums
    }

    fun getAlbumWithSongs(albumId: Int): Flow<AlbumWithSongs?> {
        val result = albumRepository.getAlbumWithSongs(albumId)
        return result.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = null
        )
    }




    class Factory(
        private val albumRepository: AlbumRepositoryImp
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AlbumViewModel::class.java)) {
                return AlbumViewModel(albumRepository) as T
            } else {
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }
}