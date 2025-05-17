package com.example.hybridmusicapp.ui.home.album

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.hybridmusicapp.data.model.album.Album
import com.example.hybridmusicapp.data.repository.album.AlbumRepositoryImp
import com.example.hybridmusicapp.data.source.remote.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlbumViewModel(
    private val albumRepository: AlbumRepositoryImp
) : ViewModel() {
    private val _albums = MutableLiveData<List<Album>?>()
    val albums = _albums

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