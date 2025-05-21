package com.example.hybridmusicapp.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.hybridmusicapp.ResultCallback
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

    fun getTop10AlbumsFireStore() {
        viewModelScope.launch(Dispatchers.IO) {
            albumRepository.getTop10AlbumsFireStore(object : ResultCallback<Result<List<Album>>> {
                override fun onResult(result: Result<List<Album>>) {
                    if(result is Result.Success){
                        _albums.postValue(result.data)
                    }else if(result is Result.Failure){
                        _albums.postValue(emptyList())
                    }
                }
            })
        }
    }

    fun getAlbumsFireStore(){
        viewModelScope.launch(Dispatchers.IO) {
            albumRepository.getAlbumsFireStore(object : ResultCallback<Result<List<Album>>> {
                override fun onResult(result: Result<List<Album>>) {
                    if(result is Result.Success){
                        _albums.postValue(result.data)
                    }else if(result is Result.Failure){
                        _albums.postValue(emptyList())
                    }
                }
            })
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