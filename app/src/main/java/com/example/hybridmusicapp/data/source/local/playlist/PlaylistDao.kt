package com.example.hybridmusicapp.data.source.local.playlist

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.hybridmusicapp.data.model.playlist.Playlist
import com.example.hybridmusicapp.data.model.playlist.PlaylistSongCrossRef
import com.example.hybridmusicapp.data.model.playlist.PlaylistWithSongs
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {
    @get:Query("SELECT * FROM playlists")
    val playlists: Flow<List<Playlist>>

    @Query("SELECT * FROM playlists")
    fun getAllPlaylistWithSongs(): Flow<List<PlaylistWithSongs>>

    @Query("SELECT * FROM playlists WHERE playlist_id = :id")
    fun getPlaylistWithSongByPlaylistId(id: Int): Flow<PlaylistWithSongs>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(playlist: Playlist)

    @Insert
    suspend fun insertPlaylistSongCrossRef(obj: PlaylistSongCrossRef): Long

    @Delete
    suspend fun delete(playlist: Playlist)

    @Update
    suspend fun update(playlist: Playlist)

}