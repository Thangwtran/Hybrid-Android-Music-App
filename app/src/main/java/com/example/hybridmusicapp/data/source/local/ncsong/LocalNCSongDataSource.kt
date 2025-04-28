package com.example.hybridmusicapp.data.source.local.ncsong

import com.example.hybridmusicapp.data.model.song.NCSong
import com.example.hybridmusicapp.data.source.NCSongDataSource
import kotlinx.coroutines.flow.Flow

class LocalNCSongDataSource (
    private val ncSongDao: NCSongDao
):NCSongDataSource{
    override suspend fun getNCSongs(): List<NCSong> {
        TODO("Not yet implemented")
    }

    override suspend fun getNCSongById(id: Int): NCSong {
        TODO("Not yet implemented")
    }

    override fun getFavouriteNCSongs(): Flow<List<NCSong>> {
        TODO("Not yet implemented")
    }

    override suspend fun insert(vararg ncSongs: NCSong): LongArray {
        TODO("Not yet implemented")
    }

    override suspend fun update(ncSong: NCSong) {
        TODO("Not yet implemented")
    }

    override suspend fun delete(ncSong: NCSong) {
        TODO("Not yet implemented")
    }
}