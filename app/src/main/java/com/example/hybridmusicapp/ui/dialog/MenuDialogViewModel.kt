package com.example.hybridmusicapp.ui.dialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.hybridmusicapp.data.model.song.Song

class MenuDialogViewModel : ViewModel() {
    private val _menuItems = MutableLiveData<List<MenuItem>>()

    private val _song = MutableLiveData<Song>()

    val menuItems: LiveData<List<MenuItem>>
        get() = _menuItems

    val song: LiveData<Song>
        get() = _song

    fun setMenuItems(items: List<MenuItem>){
        _menuItems.postValue(items)
    }

    fun setSong(song: Song){
        _song.postValue(song)
    }
}