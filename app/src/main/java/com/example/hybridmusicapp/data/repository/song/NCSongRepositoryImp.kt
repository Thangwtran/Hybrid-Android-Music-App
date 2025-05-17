package com.example.hybridmusicapp.data.repository.song

import com.example.hybridmusicapp.data.model.song.NCSong
import com.example.hybridmusicapp.data.source.NCSongDataSource
import com.example.hybridmusicapp.data.source.local.ncsong.LocalNCSongDataSource
import kotlinx.coroutines.flow.Flow

class NCSongRepositoryImp(
    private val localDataSource: LocalNCSongDataSource
) : NCSongDataSource {
    override suspend fun getNCSongs(): List<NCSong> {
        return localDataSource.getNCSongs()
    }

    override suspend fun getNCSongById(id: Int): NCSong {
        return localDataSource.getNCSongById(id)
    }

    override fun getFavouriteNCSongs(): Flow<List<NCSong>> {
        return localDataSource.getFavouriteNCSongs()
    }

    override suspend fun insert(ncSongs: List<NCSong>): LongArray {
        return localDataSource.insert(ncSongs)
    }

    override suspend fun update(ncSong: NCSong) {
        return localDataSource.update(ncSong)
    }

    override suspend fun delete(ncSong: NCSong) {
        return localDataSource.delete(ncSong)
    }

}