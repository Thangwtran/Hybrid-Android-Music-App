package com.example.hybridmusicapp.ui.now_playing

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.hybridmusicapp.data.model.song.Song
import com.example.hybridmusicapp.data.repository.song.SongRepositoryImp

class MiniPlayerViewModel(
    private val songRepository: SongRepositoryImp
): PlayerViewModel() {
    private val _isPlaying = MutableLiveData<Boolean>()
    val isPlaying = _isPlaying

    private val _currentSong = MutableLiveData<Song>()
    val currentSong = _currentSong

    var isPlaylistChanged = false


    override fun setPlayingState(state: Boolean) {
        _isPlaying.value = state
    }


    class Factory(
        private val songRepository: SongRepositoryImp
    ): ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(MiniPlayerViewModel::class.java)){
                return MiniPlayerViewModel(songRepository) as T
            }else{
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }
}