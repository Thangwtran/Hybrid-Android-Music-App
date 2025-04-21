package com.example.hybridmusicapp.data.source.local.ncsong

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.hybridmusicapp.data.model.song.NCSong
import kotlinx.coroutines.flow.Flow

@Dao
interface NCSongDao {

    @Query("SELECT * FROM nc_songs")
    suspend fun getNCSongs(): List<NCSong>

    @Query("SELECT * FROM nc_songs WHERE ncs_song_id = :id")
    suspend fun getNCSongById(id: Int): NCSong

    @Query("SELECT * FROM nc_songs WHERE is_favourite = 1")
    fun getFavouriteNCSongs(): Flow<List<NCSong>>

    @Insert
    suspend fun insert(vararg ncSongs: NCSong): LongArray

    @Update
    suspend fun update(ncSong: NCSong)

    @Delete
    suspend fun delete(ncSong: NCSong)
}