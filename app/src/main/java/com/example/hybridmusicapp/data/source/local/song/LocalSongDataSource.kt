package com.example.hybridmusicapp.data.source.local.song

import com.example.hybridmusicapp.data.SongDataSource
import com.example.hybridmusicapp.data.model.song.Song
import kotlinx.coroutines.flow.Flow

class LocalSongDataSource(
    private val songDao: SongDao
): SongDataSource.Local {
    override suspend fun getLocalSongs(): List<Song> {
        return songDao.getSongs()
    }

    override fun getFavouriteSongs(): Flow<List<Song>> {
        return songDao.getFavouriteSongs()
    }

    override fun getTop30Replay(): Flow<List<Song>> {
        return songDao.getTop30Replay()
    }

    override suspend fun getByTitle(title: String): List<Song> {
        return songDao.getByTitle(title)
    }

    override suspend fun getById(id: String): Song {
        return songDao.getById(id)
    }

    override suspend fun insert(vararg songs: Song): LongArray {
        return songDao.insert(*songs)
    }

    override suspend fun update(song: Song) {
        return songDao.update(song)
    }

    override suspend fun delete(song: Song) {
        return songDao.delete(song)
    }

}