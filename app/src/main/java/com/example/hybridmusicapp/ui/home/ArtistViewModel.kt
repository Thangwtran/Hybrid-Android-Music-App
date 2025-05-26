package com.example.hybridmusicapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.hybridmusicapp.ResultCallback
import com.example.hybridmusicapp.data.model.artist.Artist
import com.example.hybridmusicapp.data.repository.artist.ArtistRepositoryImp
import com.example.hybridmusicapp.data.source.remote.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ArtistViewModel(
    private val artistRepository: ArtistRepositoryImp
) : ViewModel() {
    private val _artists = MutableLiveData<List<Artist>?>()
    val remoteArtists: LiveData<List<Artist>?> = _artists

    val localArtists: LiveData<List<Artist>>
        get() = artistRepository.getLocalArtists().asLiveData()

    fun getArtists() {
        viewModelScope.launch(Dispatchers.IO) {
            artistRepository.getArtistFirebase(object : ResultCallback<Result<List<Artist>>> {
                override fun onResult(result: Result<List<Artist>>) {
                    if (result is Result.Success) {
                        _artists.postValue(result.data)
                    } else if (result is Result.Failure) {
                        _artists.postValue(null)
                    }
                }
            })
        }
    }

    fun getTop20Artists() {
        viewModelScope.launch(Dispatchers.IO) {
            artistRepository.getTop20ArtistFirebase(object : ResultCallback<Result<List<Artist>>> {
                override fun onResult(result: Result<List<Artist>>) {
                    if (result is Result.Success) {
                        _artists.postValue(result.data)
                    } else if (result is Result.Failure) {
                        _artists.postValue(null)
                    }
                }
            })
        }
    }

    class Factory(
        private val artistRepository: ArtistRepositoryImp
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ArtistViewModel::class.java)) {
                return ArtistViewModel(artistRepository) as T
            } else {
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }
}