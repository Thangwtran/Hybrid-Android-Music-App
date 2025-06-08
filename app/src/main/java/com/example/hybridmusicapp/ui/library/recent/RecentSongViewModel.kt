package com.example.hybridmusicapp.ui.library.recent

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.hybridmusicapp.data.model.song.NCSong
import com.example.hybridmusicapp.data.model.song.Song
import com.example.hybridmusicapp.ui.viewmodel.NowPlayingViewModel

class RecentSongViewModel : ViewModel() {
    private val _recentSongs = MutableLiveData<List<Song>>()
    private val _recentNcs = MutableLiveData<List<NCSong>>()

    val recentSong: LiveData<List<Song>>
        get() = _recentSongs

    val recentNcsSong: LiveData<List<NCSong>>
        get() = _recentNcs

    fun setRecentSong(songs: List<Song>) {
        _recentSongs.value = songs
    }

    fun setRecentNcs(ncs: List<NCSong>) {
        _recentNcs.value = ncs
    }
}