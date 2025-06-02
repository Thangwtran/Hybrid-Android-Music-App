package com.example.hybridmusicapp.ui.viewmodel


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.media3.session.MediaController

class MediaViewModel private constructor(): ViewModel() {

    private val _mediaController = MutableLiveData<MediaController>()

    val mediaController: MutableLiveData<MediaController>
        get() = _mediaController

    fun setMediaController(mediaController: MediaController) {
        _mediaController.value = mediaController
    }

    companion object{
        val instance = MediaViewModel()
    }
}