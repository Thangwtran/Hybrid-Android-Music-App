package com.example.hybridmusicapp.ui.now_playing

import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService

class PlaybackService: MediaSessionService() {
    private lateinit var mediaSession: MediaSession

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {
        TODO("Not yet implemented")
    }
}