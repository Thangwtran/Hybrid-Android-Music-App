package com.example.hybridmusicapp.data.source.local.ncsong

import com.example.hybridmusicapp.data.model.song.NCSong
import com.example.hybridmusicapp.data.source.NCSongDataSource
import kotlinx.coroutines.flow.Flow

class LocalNCSongDataSource (
    private val ncSongDao: NCSongDao
):NCSongDataSource{
    override suspend fun getNCSongs(): List<NCSong> {
        return ncSongDao.getNCSongs()
    }

    override suspend fun getNCSongById(id: Int): NCSong {
        return ncSongDao.getNCSongById(id)
    }

    override fun getFavouriteNCSongs(): Flow<List<NCSong>> {
        return ncSongDao.getFavouriteNCSongs()
    }

    override suspend fun insert(ncSongs: List<NCSong>): LongArray {
        return ncSongDao.insert(ncSongs)
    }

    override suspend fun update(ncSong: NCSong) {
        ncSongDao.update(ncSong)
    }

    override suspend fun delete(ncSong: NCSong) {
        ncSongDao.delete(ncSong)
    }
}