package com.example.hybridmusicapp.data.repository.playlist

import com.example.hybridmusicapp.data.model.playlist.Playlist
import com.example.hybridmusicapp.data.model.playlist.PlaylistSongCrossRef
import com.example.hybridmusicapp.data.model.playlist.PlaylistWithSongs
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    fun getAllPlaylists(): Flow<List<Playlist>>

    fun getAllPlaylistWithSongs(): Flow<List<PlaylistWithSongs>>

    suspend fun insertPlaylistSongCrossRef(obj: PlaylistSongCrossRef): Boolean

    suspend fun getPlaylistWithSongByPlaylistId(id: Int): Flow<PlaylistWithSongs>

    suspend fun insertPlaylist(playlist: Playlist)

    suspend fun deletePlaylist(playlist: Playlist)

    suspend fun updatePlaylist(playlist: Playlist)
}