package com.example.hybridmusicapp.ui.discovery.artist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.hybridmusicapp.data.model.artist.Artist
import com.example.hybridmusicapp.data.repository.artist.ArtistRepositoryImp
import com.example.hybridmusicapp.data.source.remote.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ArtistViewModel(
    private val artistRepository: ArtistRepositoryImp
) : ViewModel() {
    private val _artists = MutableLiveData<List<Artist>?>()
    val remoteArtist = _artists

    fun loadRemoteArtist() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = artistRepository.loadRemoteArtists()
            if (result is Result.Success) {
                _artists.postValue(result.data.artists)
            } else if (result is Result.Failure) {
                // TODO: Handle exception
            }
        }
    }

    fun addArtistsToFireStore(artists: List<Artist>) {
        viewModelScope.launch(Dispatchers.IO) {
            artistRepository.addArtistToFireStore(artists)
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