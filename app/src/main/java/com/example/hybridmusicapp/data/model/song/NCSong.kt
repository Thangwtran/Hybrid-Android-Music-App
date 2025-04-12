package com.example.hybridmusicapp.data.model.song

data class NCSong(
    val id: Int,
    val name: String,
    val artist: String,
    val genre: String,
    val imageRes: Int,
    val audioRes: Int,
)