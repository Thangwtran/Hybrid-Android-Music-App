package com.example.hybridmusicapp.data.source.local.album

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.hybridmusicapp.data.model.album.Album
import kotlinx.coroutines.flow.Flow

@Dao
interface AlbumDao {
    @get:Query("SELECT * FROM albums ORDER BY size LIMIT 10")
    val top10Albums: List<Album> // must have @get because it is property

//    @Transaction
//    @Query("SELECT * FROM albums WHERE ")
//    fun getAlbumWithSongs(id: Int): Flow<AlbumWithSongs>)

    @get:Query("SELECT * FROM albums ORDER BY size")
    val albums: List<Album>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun saveAlbums(vararg album: Album): LongArray

    // TODO: save album cross ref
    @Update
    suspend fun updateAlbum(album: Album)

    @Delete
    suspend fun deleteAlbum(album: Album)
}