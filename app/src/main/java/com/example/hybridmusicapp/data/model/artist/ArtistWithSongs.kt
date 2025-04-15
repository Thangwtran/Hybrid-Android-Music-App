package com.example.hybridmusicapp.data.model.artist

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.hybridmusicapp.data.model.song.Song

data class ArtistWithSongs(
    @Embedded
    var artist: Artist? = null,

    @Relation(
        parentColumn = "artist_id",
        entityColumn = "song_id",
        associateBy = Junction(
            ArtistSongCrossRef::class
        )
    )
    var songs: List<Song> = listOf()
)