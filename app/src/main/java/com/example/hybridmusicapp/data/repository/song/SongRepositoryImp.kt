package com.example.hybridmusicapp.data.repository.song

import com.example.hybridmusicapp.ResultCallback
import com.example.hybridmusicapp.data.model.song.Song
import com.example.hybridmusicapp.data.model.song.SongList
import com.example.hybridmusicapp.data.source.SongDataSource
import com.example.hybridmusicapp.data.source.remote.RemoteSongDataSource
import kotlinx.coroutines.flow.Flow
import com.example.hybridmusicapp.data.source.remote.Result

class SongRepositoryImp(
    private val localDataSource: SongDataSource.Local
) : SongRepository.Local, SongRepository.Remote {
    private val remoteDataSource: SongDataSource.Remote = RemoteSongDataSource()

    // LOCAL
    override suspend fun getSongList(): List<Song> {
        return localDataSource.getLocalSongs()
    }

    override val favouriteSongs: Flow<List<Song>>
        get() = localDataSource.getFavouriteSongs()

    override val top30Replay: Flow<List<Song>>
        get() = localDataSource.getTop30Replay()

    override suspend fun getByTitle(title: String): List<Song> {
        return localDataSource.getByTitle(title)
    }

    override suspend fun getById(songId: String): Song {
        return localDataSource.getById(songId)
    }

    override suspend fun insert(vararg songs: Song): Boolean {
        return localDataSource.insert(*songs)
    }

    override suspend fun update(song: Song) {
        return localDataSource.update(song)
    }

    override suspend fun delete(song: Song) {
        return localDataSource.delete(song)
    }


    // REMOTE
    override suspend fun loadRemoteSongs(): Result<SongList> {
        return remoteDataSource.loadRemoteSongs()
    }

    override suspend fun getSongByArtist(artist: String): Result<List<Song>> {
        return remoteDataSource.getSongByArtist(artist)
    }

    override suspend fun getSongByTitle(title: String): Result<List<Song>> {
        return remoteDataSource.getSongByTitle(title)
    }

    override suspend fun getSongById(
        songId: String,
        callback: ResultCallback<Result<Song>>
    ) {
        return remoteDataSource.getSongById(songId, callback)
    }

    override suspend fun getTop10MostHeard(callback: ResultCallback<Result<List<Song>>>) {
        remoteDataSource.getTop10MostHeard(callback)
    }

    override suspend fun getTop15Replay(callback: ResultCallback<Result<List<Song>>>) {
        remoteDataSource.getTop15Replay(callback)
    }

    override suspend fun updateSongCounter(songId: String) {
        remoteDataSource.updateSongCounter(songId)
    }

    override suspend fun addSongToFireStore(songs: List<Song>) {
        remoteDataSource.addSongToFireStore(songs)
    }
}