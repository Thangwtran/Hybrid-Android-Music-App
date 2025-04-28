package com.example.hybridmusicapp.data.source


import com.example.hybridmusicapp.ResultCallback
import com.example.hybridmusicapp.data.model.song.Song
import com.example.hybridmusicapp.data.model.song.SongList
import kotlinx.coroutines.flow.Flow
import com.example.hybridmusicapp.data.source.remote.Result

interface SongDataSource {
    interface Local {
        suspend fun getLocalSongs(): List<Song>
        fun getFavouriteSongs(): Flow<List<Song>>
        fun getTop30Replay(): Flow<List<Song>>
        suspend fun getByTitle(title: String): List<Song>
        suspend fun getById(id: String): Song
        suspend fun insert(vararg songs: Song): LongArray
        suspend fun update(song: Song)
        suspend fun delete(song: Song)
    }

    interface Remote {
        suspend fun loadRemoteSongs():Result<SongList> // load remote
        suspend fun getSongByArtist(artist: String): Result<List<Song>>
        suspend fun getSongByTitle(title: String): Result<List<Song>>
        suspend fun getSongById(songId: String, callback: ResultCallback<Result<Song>>)
        suspend fun getTop10MostHeard(callback: ResultCallback<Result<List<Song>>>)
        suspend fun getTop10Replay(callback: ResultCallback<Result<List<Song>>>)
        suspend fun updateSongCounter(songId: String)
        suspend fun addSongToFireStore(songs: List<Song>)
    }
}