package com.example.hybridmusicapp.ui.library

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.example.hybridmusicapp.data.model.song.NCSong
import com.example.hybridmusicapp.data.model.song.Song
import com.example.hybridmusicapp.data.repository.recent_song.RecentSongRepositoryImp
import com.example.hybridmusicapp.data.repository.song.SongRepositoryImp

class LibraryViewModel(
    private val songRepository: SongRepositoryImp,
    private val recentSongRepository: RecentSongRepositoryImp
) : ViewModel() {

    val favouriteSongs: LiveData<List<Song>>
        get() = songRepository.favouriteSongs.asLiveData()

    val recentSongs: LiveData<List<Song>>
        get() = recentSongRepository.recentSongs.asLiveData()

    val recentNcsSongs: LiveData<List<NCSong>>
        get() = recentSongRepository.recentNcsSongs.asLiveData()

    class Factory(
        private val songRepository: SongRepositoryImp,
        private val recentSongRepository: RecentSongRepositoryImp
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(LibraryViewModel::class.java)) {
                return LibraryViewModel(songRepository, recentSongRepository) as T
            } else {
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }
}