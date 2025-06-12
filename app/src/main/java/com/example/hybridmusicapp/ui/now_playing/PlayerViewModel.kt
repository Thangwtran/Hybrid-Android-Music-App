package com.example.hybridmusicapp.ui.now_playing

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
open class PlayerViewModel @Inject constructor() : ViewModel() {
    private val _mediaItems = MutableLiveData<List<MediaItem>>()
    val mediaItems: LiveData<List<MediaItem>> = _mediaItems

    private val _playingState = MutableLiveData<Boolean>()
    val playingState: LiveData<Boolean> = _playingState

    open fun setPlayingState(state: Boolean) {
        _playingState.value = state
    }

    fun setMediaItems(items: List<MediaItem>) {
        _mediaItems.value = items
    }

    fun miliToSecond(value: Long): String {
        val minute = value / 60000
        val second = (value / 1000) % 60
        return if (value < 0 || value > Int.MAX_VALUE) "00:00"
        else String.format(Locale.ENGLISH, "%02d:%02d", minute, second)
    }

}