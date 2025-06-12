package com.example.hybridmusicapp.ui.searching

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.example.hybridmusicapp.ResultCallback
import com.example.hybridmusicapp.data.model.artist.Artist
import com.example.hybridmusicapp.data.model.history.HistorySearchedKey
import com.example.hybridmusicapp.data.model.song.Song
import com.example.hybridmusicapp.data.repository.search.SearchingRepositoryImp
import com.example.hybridmusicapp.ui.viewmodel.NowPlayingViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchingViewModel @Inject constructor(
    private val searchRepo: SearchingRepositoryImp
) : ViewModel() {
    private val _songs = MutableLiveData<List<Song>>()
    private val _selectedSongs = MutableLiveData<List<Song>>()
    private val _artists = MutableLiveData<List<Artist>>()
    private val _keys = MutableLiveData<List<HistorySearchedKey>>()
    private val nowPlayingViewModel = NowPlayingViewModel.instance!!
    val currentSearchKey: String? = null


    // get set
    val historySearchedKeys: LiveData<List<HistorySearchedKey>>
        get() = searchRepo.historySearchedKeys.asLiveData()

    val historySearchedSongs: LiveData<List<Song>>
        get() = searchRepo.historySearchedSongs.asLiveData()

    val songs: LiveData<List<Song>>
        get() = _songs

    fun setSongs(songs: List<Song>) {
        _songs.postValue(songs)
    }

    val selectedSongs: LiveData<List<Song>>
        get() = _selectedSongs

    fun setSelectedSongs(songs: List<Song>) {
        _selectedSongs.postValue(songs)
    }

    val artists: LiveData<List<Artist>>
        get() = _artists

    fun setArtists(artists: List<Artist>) {
        _artists.postValue(artists)
    }

    val keys: LiveData<List<HistorySearchedKey>>
        get() = _keys

    fun setKeys(keys: List<HistorySearchedKey>) {
        _keys.postValue(keys)
    }



    // function
    fun search(key: String, callBack: ResultCallback<List<Song>>) {}

    fun updatePlaylist(song: Song) {}

    fun insertRecentSearchedSong(song: Song) {}

    fun insertSearchedKey(key: String) {}

    fun clearHistorySongs() {}

    fun clearHistoryKeys() {}

    internal class Factory @Inject constructor(
        private val searchRepo: SearchingRepositoryImp
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SearchingViewModel::class.java)) {
                return SearchingViewModel(searchRepo) as T
            } else {
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }
}