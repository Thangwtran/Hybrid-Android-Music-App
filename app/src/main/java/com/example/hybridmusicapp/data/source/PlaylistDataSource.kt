package com.example.hybridmusicapp.data.source

import com.example.hybridmusicapp.data.model.playlist.Playlist
import kotlinx.coroutines.flow.Flow

// Local
interface PlaylistDataSource {
    fun getAllPlaylist(): Flow<List<Playlist>>
    suspend fun insert(playlist: Playlist)
    suspend fun deletePlaylist(playlist: Playlist)
    suspend fun updatePlaylist(playlist: Playlist)
}