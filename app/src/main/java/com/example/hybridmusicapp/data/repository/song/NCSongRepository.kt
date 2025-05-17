package com.example.hybridmusicapp.data.repository.song

import com.example.hybridmusicapp.data.model.song.NCSong
import kotlinx.coroutines.flow.Flow

interface NCSongRepository {
    interface Local{
        suspend fun getNCSongs():List<NCSong>
        val favouriteNCSongs: Flow<List<NCSong>>
        suspend fun getNCSongById(id:Int):NCSong
        suspend fun insert(vararg ncSongs:NCSong):LongArray
        suspend fun update(ncSong:NCSong)
        suspend fun delete(ncSong:NCSong)
    }
}