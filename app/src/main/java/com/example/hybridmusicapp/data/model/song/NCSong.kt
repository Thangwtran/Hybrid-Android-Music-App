package com.example.hybridmusicapp.data.model.song

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "nc_songs" )
data class NCSong(
    @PrimaryKey()
    @ColumnInfo(name = "ncs_song_id")
    val id: Int = 0,

    @ColumnInfo(name = "ncs_name")
    val ncsName: String,

    @ColumnInfo(name = "ncs_artist")
    val artist: String,

    @ColumnInfo(name = "ncs_genre")
    val genre: String,

    @ColumnInfo(name = "ncs_description")
    val description: String,

    @ColumnInfo(name = "ncs_image_res")
    val imageRes: Int,

    @ColumnInfo(name = "ncs_audio_res")
    val audioRes: Int,

    @ColumnInfo(name = "is_favourite")
    var isFavourite:Boolean = false,
)