package com.example.hybridmusicapp.data.model.song

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey


@Entity(tableName = "nc_songs")
open class NCSong(
    @PrimaryKey
    @ColumnInfo(name = "ncs_song_id")
    var id: Int = 0,

    @ColumnInfo(name = "ncs_name")
    var ncsName: String="",

    @ColumnInfo(name = "ncs_artist")
    var artist: String="",

    @ColumnInfo(name = "ncs_genre")
    var genre: String = "",

    @ColumnInfo(name = "ncs_description")
    var description: String = "",

    @ColumnInfo(name = "ncs_image_res")
    var imageRes: Int = -1,

    @ColumnInfo(name = "ncs_audio_res")
    var audioRes: Int = 0,

    @ColumnInfo(name = "is_favourite")
    var isFavourite: Boolean = false,

    @ColumnInfo(name = "num_of_interest", defaultValue = "0")
    var numOfInterest: Int = 0
)