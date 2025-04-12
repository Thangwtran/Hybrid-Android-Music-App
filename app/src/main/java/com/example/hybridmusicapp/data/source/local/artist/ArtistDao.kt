package com.example.hybridmusicapp.data.source.local.artist

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.hybridmusicapp.data.model.artist.Artist
import kotlinx.coroutines.flow.Flow

interface ArtistDao {
    @get:Query("SELECT * FROM artists ORDER BY interested")
    val artists: Flow<List<Artist>>

    // TODO: artist with songs

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArtist(vararg artist: Artist)

    // TODO: insert artist cross ref

    @Delete
    suspend fun deleteArtist(artist: Artist)
    @Update
    suspend fun updateArtist(artist: Artist)

}