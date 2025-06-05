package com.example.hybridmusicapp.data.source.local.artist

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.hybridmusicapp.data.model.artist.Artist
import com.example.hybridmusicapp.data.model.artist.ArtistSongCrossRef
import com.example.hybridmusicapp.data.model.artist.ArtistWithSongs
import kotlinx.coroutines.flow.Flow

@Dao
interface ArtistDao {
    @get:Query("SELECT * FROM artists ORDER BY interested")
    val artists: Flow<List<Artist>>

    @Transaction
    @Query("SELECT * FROM artists WHERE artist_id = :artistId")
    suspend fun getArtistWithSongs(artistId: Int): ArtistWithSongs

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArtist(vararg artist: Artist)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArtistSongCrossRef(vararg values: ArtistSongCrossRef)

    @Delete
    suspend fun deleteArtist(artist: Artist)

    @Update
    suspend fun updateArtist(artist: Artist)

}