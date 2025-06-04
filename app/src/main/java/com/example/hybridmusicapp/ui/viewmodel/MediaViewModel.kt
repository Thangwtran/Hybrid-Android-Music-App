package com.example.hybridmusicapp.ui.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaController
import androidx.media3.session.MediaSession

class MediaViewModel private constructor(): ViewModel() {

    private val _mediaController = MutableLiveData<MediaController>()

    val mediaController: MutableLiveData<MediaController>
        get() = _mediaController

    private val _audioSession = MutableLiveData<Int>()
    val audioSession: LiveData<Int>
        get() = _audioSession

    fun setAudioSession(audioSession: Int) {
        _audioSession.postValue(audioSession)
    }

    fun setMediaController(mediaController: MediaController) {
        _mediaController.value = mediaController
    }

    companion object{
        private var _instance: MediaViewModel? = null
        val instance: MediaViewModel
            get() {
                if (_instance == null) {
                    _instance = MediaViewModel()
                }
                return _instance!!
            }
    }
}