package com.example.hybridmusicapp.ui.dialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.hybridmusicapp.data.model.song.Song

class SongInfoDialogViewModel: ViewModel() {
    private val _song = MutableLiveData<Song>()

    val song: LiveData<Song>
        get() = _song

    fun getSong(song:Song){
        _song.postValue(song)
    }

}