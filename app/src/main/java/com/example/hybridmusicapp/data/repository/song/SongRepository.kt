package com.example.hybridmusicapp.data.repository.song

import com.example.hybridmusicapp.data.model.song.Song
import kotlinx.coroutines.flow.Flow

interface SongRepository {
    interface Remote {

    }

    interface Local {
        suspend fun getSongList(): List<Song>
        val favouriteSongs: Flow<List<Song>>
        val top30Replay: Flow<List<Song>>
        suspend fun getByTitle(title: String): List<Song>
        suspend fun getById(songId: String): Song
        suspend fun insert(vararg songs: Song): Boolean
        suspend fun update(song: Song)
        suspend fun delete(song: Song)
    }
}