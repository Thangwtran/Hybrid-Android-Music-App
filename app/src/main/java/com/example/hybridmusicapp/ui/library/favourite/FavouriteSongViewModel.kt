package com.example.hybridmusicapp.ui.library.favourite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.hybridmusicapp.data.model.song.Song

class FavouriteSongViewModel: ViewModel() {
    private val _favouriteSongs = MutableLiveData<List<Song>>()

    val favouriteSongs: LiveData<List<Song>>
        get() = _favouriteSongs

    fun setFavouriteSongs(songs: List<Song>) {
        _favouriteSongs.value = songs
    }
}