package com.example.hybridmusicapp.ui.now_playing

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem

open class PlayerViewModel: ViewModel(){
    private val _mediaItems = MutableLiveData<List<MediaItem>>()
    val mediaItems = _mediaItems

    private val _playingState = MutableLiveData<Boolean>()
    val playingState = _playingState

    open fun setPlayingState(state: Boolean){
        _playingState.value = state
    }

    fun setMediaItems(items: List<MediaItem>){
        _mediaItems.value = items
    }

}