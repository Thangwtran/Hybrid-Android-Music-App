package com.example.hybridmusicapp.data.model.album

import com.google.gson.annotations.SerializedName

data class AlbumList(
    @SerializedName("playlists")
    val playlist: List<Album> = listOf()
)
