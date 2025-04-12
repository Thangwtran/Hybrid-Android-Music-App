package com.example.hybridmusicapp.data.model.song

import com.google.gson.annotations.SerializedName

@Suppress("unused")
data class SongList(
    @SerializedName("songs")
    var songs: List<Song> = listOf()
)
