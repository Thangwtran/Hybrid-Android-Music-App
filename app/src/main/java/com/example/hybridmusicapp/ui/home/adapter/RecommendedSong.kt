package com.example.hybridmusicapp.ui.home.adapter

data class RecommendedSong(
    var imageRes: Int = 0,
    var remoteImageRes: String = "",
    val title: String,
    val artist: String,
    var ncsAudioRes: Int = 0,
    var remoteAudioUrl: String = ""
)
