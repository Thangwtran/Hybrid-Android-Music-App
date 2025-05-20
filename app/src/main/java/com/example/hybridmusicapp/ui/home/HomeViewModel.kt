package com.example.hybridmusicapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.hybridmusicapp.ResultCallback
import com.example.hybridmusicapp.data.model.song.Song
import com.example.hybridmusicapp.data.repository.album.AlbumRepository
import com.example.hybridmusicapp.data.repository.album.AlbumRepositoryImp
import com.example.hybridmusicapp.data.repository.song.SongRepository
import com.example.hybridmusicapp.data.repository.song.SongRepositoryImp
import com.example.hybridmusicapp.data.source.remote.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(
    private val albumRepository: AlbumRepositoryImp,
    private val songRepository: SongRepositoryImp,
) : ViewModel() {
    private var dispatcher: CoroutineDispatcher = Dispatchers.IO

    private val _remoteSongs =  MutableLiveData<List<Song>?>()
    val remoteSongs = _remoteSongs

//    private val _remoteSongLoaded = MutableLiveData<Boolean>()
//    val remoteSongLoaded: LiveData<Boolean>
//        get() = _remoteSongLoaded

    fun loadRemoteSongs() {
        viewModelScope.launch(dispatcher) {
            val result = songRepository.loadRemoteSongs()
            if (result is Result.Success) {
                _remoteSongs.postValue(result.data.songs)
//                _remoteSongLoaded.postValue(true)
            } else if (result is Result.Failure) {
                // TODO: HomeViewModel -> Handle exception
            }
        }
    }

    fun addSongToFireStore(songs: List<Song>) {
        viewModelScope.launch(dispatcher) {
            songRepository.addSongToFireStore(songs)
        }
    }

    fun getTop10MostHeard(){
        viewModelScope.launch(dispatcher) {
            songRepository.getTop10MostHeard(object : ResultCallback<Result<List<Song>>>{
                override fun onResult(result: Result<List<Song>>) {
                    if(result is Result.Success){
//                        _remoteSongs.clear()
                        _remoteSongs.postValue(result.data)
//                        _remoteSongLoaded.postValue(true)
                    }else if(result is Result.Failure){
//                        _remoteSongLoaded.postValue(false)
                        _remoteSongs.postValue(emptyList())
                    }
                }
            })
        }
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