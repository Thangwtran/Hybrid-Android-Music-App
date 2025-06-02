package com.example.hybridmusicapp.data.model.playlist

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.hybridmusicapp.data.model.song.NCSong
import com.example.hybridmusicapp.data.model.song.Song

data class PlaylistWithSongs(
    @Embedded
    var playlist: Playlist? = null,

    @Relation(
        parentColumn = "playlist_id",
        entityColumn = "song_id",
        associateBy = Junction(PlaylistSongCrossRef::class)
    )
    var songs:List<Song> = listOf()
)