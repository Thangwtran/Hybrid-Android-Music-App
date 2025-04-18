package com.example.hybridmusicapp.data.source.local.song

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.hybridmusicapp.data.model.song.Song
import kotlinx.coroutines.flow.Flow

interface SongDao {
    @Query(
        "SELECT song_id, title, album, artist, source, image, duration, " +
                "favorite, counter, replay FROM songs"
    )
    suspend fun getSongs(): List<Song>

    @Query(
        "SELECT song_id,title,album,artist,source,image,duration," +
                "favorite,counter,replay FROM songs WHERE favorite = 1"
    )
    fun getFavouriteSongs(): Flow<List<Song>>

    @Query(
        "SELECT song_id, title, album, artist, source, image, duration, favorite, counter, replay " +
                "FROM songs ORDER BY replay DESC LIMIT 30"
    )
    fun getTop30Replay(): Flow<List<Song>>

    @Query(
        "SELECT song_id, title, album, artist, source, image, duration, favorite, counter, replay " +
                "FROM songs WHERE title LIKE :title"
    )
    suspend fun getByTitle(title: String): List<Song> // get songs by title

    @Query("SELECT * FROM songs WHERE song_id = :id")
    suspend fun getById(id: String): Song // get song by id

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg songs: Song): LongArray

    @Update
    suspend fun update(song:Song)

    // TODO: update replay

    // TODO: update counter

    @Delete
    suspend fun delete(song:Song)
}