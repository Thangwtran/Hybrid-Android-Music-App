package com.example.hybridmusicapp.data.repository.playlist

import com.example.hybridmusicapp.data.model.playlist.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    fun getAllPlaylists(): Flow<List<Playlist>>

    // TODO: Playlist with songs
    // TODO: get playlist song cross ref 
    // TODO: get playlist song by playlist_id

    suspend fun insertPlaylist(playlist: Playlist)
    suspend fun deletePlaylist(playlist: Playlist)
    suspend fun updatePlaylist(playlist: Playlist)
}