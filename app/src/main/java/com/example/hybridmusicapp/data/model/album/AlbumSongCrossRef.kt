package com.example.hybridmusicapp.data.model.album

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index


@Entity(
    tableName = "album_song_cross_ref",
    primaryKeys = ["album_id","song_id"],
    indices = [Index("song_id"), Index("album_id")]
)
data class AlbumSongCrossRef(
    @ColumnInfo(name = "album_id")
    val albumId: Int = 0,

    @ColumnInfo(name = "song_id")
    val songId: String = ""
)
