package com.example.hybridmusicapp.data.repository.song

import com.example.hybridmusicapp.ResultCallback
import com.example.hybridmusicapp.data.model.song.Song
import com.example.hybridmusicapp.data.model.song.SongList
import kotlinx.coroutines.flow.Flow

interface SongRepository {
    interface Remote {
        suspend fun loadRemoteSongs(): Result<SongList>
        suspend fun getSongByArtist(artist: String): Result<List<Song>>
        suspend fun getSongByTitle(title: String): Result<List<Song>>
        suspend fun getSongById(songId: String,callback: ResultCallback<Result<Song>>): Result<Song>
        suspend fun getTop10MostHeard(callback: ResultCallback<Result<List<Song>>>): Result<List<Song>>
        suspend fun getTop10Replay(callback: ResultCallback<Result<List<Song>>>): Result<List<Song>>
        suspend fun updateSongCounter(songId: String)
        fun addSongToFireStore(songs: List<Song>)
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