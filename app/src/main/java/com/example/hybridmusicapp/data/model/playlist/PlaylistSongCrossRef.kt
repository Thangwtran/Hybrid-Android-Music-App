package com.example.hybridmusicapp.data.model.playlist

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index


@Entity(
    tableName = "playlist_song_cross_ref",
    primaryKeys = ["playlist_id", "song_id"],
    indices = [Index(value = ["playlist_id"]),Index(value = ["song_id"])]
)
data class PlaylistSongCrossRef(
    @ColumnInfo(name = "song_id")
    var songId: String = "",

    @ColumnInfo(name = "playlist_id")
    var playlistId: Int = 0
)
