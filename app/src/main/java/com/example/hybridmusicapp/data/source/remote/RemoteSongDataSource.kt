package com.example.hybridmusicapp.data.source.remote

import com.example.hybridmusicapp.ResultCallback
import com.example.hybridmusicapp.data.model.song.Song
import com.example.hybridmusicapp.data.model.song.SongList
import com.example.hybridmusicapp.data.source.SongDataSource

class RemoteSongDataSource : SongDataSource.Remote {
    override suspend fun getSongList(): Result<SongList> {
        TODO("Not yet implemented")
    }

    override suspend fun getSongByArtist(artist: String): Result<List<Song>> {
        TODO("Not yet implemented")
    }

    override suspend fun getSongByTitle(title: String): Result<List<Song>> {
        TODO("Not yet implemented")
    }

    override suspend fun getSongById(
        songId: String,
        callback: ResultCallback<Result<Song>>
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun getTop10MostHeard(callback: ResultCallback<Result<List<Song>>>) {
        TODO("Not yet implemented")
    }

    override suspend fun getTop10Replay(callback: ResultCallback<Result<List<Song>>>) {
        TODO("Not yet implemented")
    }

    override suspend fun updateSongCounter(songId: String) {
        TODO("Not yet implemented")
    }
}