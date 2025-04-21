package com.example.hybridmusicapp.data.source

import com.example.hybridmusicapp.data.model.song.Song
import kotlinx.coroutines.flow.Flow

interface SongDataSource {
    interface Local{
        suspend fun getLocalSongs():List<Song>
        fun getFavouriteSongs():Flow<List<Song>>
        fun getTop30Replay():Flow<List<Song>>
        suspend fun getByTitle(title:String):List<Song>
        suspend fun getById(id:String):Song
        suspend fun insert(vararg songs:Song):LongArray
        suspend fun update(song:Song)
        suspend fun delete(song:Song)
    }
    interface Remote{}
}