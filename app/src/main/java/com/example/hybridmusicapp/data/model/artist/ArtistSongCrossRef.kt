package com.example.hybridmusicapp.data.model.artist

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index


@Entity(
    tableName = "artist_song_cross_ref",
    primaryKeys = ["artist_id","song_id"],
    indices = [Index(value = ["artist_id"]), Index(value = ["song_id"])]
)
data class ArtistSongCrossRef(
    @ColumnInfo("artist_id")
    var artistId: Int = 0,

    @ColumnInfo("song_id")
    var songId: String = "",
)