package com.example.hybridmusicapp.ui.library.favourite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.hybridmusicapp.data.model.song.NCSong
import com.example.hybridmusicapp.data.model.song.Song

class FavouriteSongViewModel: ViewModel() {
    private val _favouriteSongs = MutableLiveData<List<Song>>()
    private val _favouriteNcs = MutableLiveData<List<NCSong>>()

    val favouriteSongs: LiveData<List<Song>>
        get() = _favouriteSongs

    val favouriteNcs: LiveData<List<NCSong>>
        get() = _favouriteNcs

    fun setFavouriteNcs(ncs: List<NCSong>) {
        _favouriteNcs.value = ncs
    }

    fun setFavouriteSongs(songs: List<Song>) {
        _favouriteSongs.value = songs
    }
}