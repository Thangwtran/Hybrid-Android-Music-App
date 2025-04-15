package com.example.hybridmusicapp.data.model.album

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.hybridmusicapp.data.model.song.Song

data class AlbumWithSongs(
    @Embedded
    val album: Album? = null,

    @Relation(
        parentColumn = "album_id",
        entityColumn = "song_id",
        associateBy = Junction(
            AlbumSongCrossRef::class
        )
    )
    val songs: List<Song> = listOf()
)