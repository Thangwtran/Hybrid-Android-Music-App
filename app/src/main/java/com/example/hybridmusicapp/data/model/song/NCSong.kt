package com.example.hybridmusicapp.data.model.song

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


@Entity(tableName = "nc_songs" )
data class NCSong(
    @PrimaryKey
    @ColumnInfo(name = "ncs_song_id", defaultValue = "")
    val id: Int,

    @ColumnInfo(name = "ncs_title")
    val title: String,

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
    val isFavourite:Boolean = false,
)