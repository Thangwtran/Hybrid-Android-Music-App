package com.example.hybridmusicapp.data.source

import com.example.hybridmusicapp.data.model.song.NCSong
import kotlinx.coroutines.flow.Flow

// Local
interface NCSongDataSource {
    suspend fun getNCSongs():List<NCSong>
    suspend fun getNCSongById(id:Int):NCSong
    fun getFavouriteNCSongs(): Flow<List<NCSong>>
    suspend fun insert(vararg ncSongs:NCSong):LongArray
    suspend fun update(ncSong:NCSong)
    suspend fun delete(ncSong:NCSong)
}