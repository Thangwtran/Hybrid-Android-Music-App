package com.example.hybridmusicapp.data.source

import com.example.hybridmusicapp.data.model.playlist.Playlist
import com.example.hybridmusicapp.data.model.playlist.PlaylistSongCrossRef
import com.example.hybridmusicapp.data.model.playlist.PlaylistWithSongs
import kotlinx.coroutines.flow.Flow

// Local
interface PlaylistDataSource {
    fun getAllPlaylist(): Flow<List<Playlist>>
    fun getAllPlaylistWithSongs(): Flow<List<PlaylistWithSongs>>
    fun getPlaylistWithSongByPlaylistId(id: Int): Flow<PlaylistWithSongs>
    suspend fun insertPlaylistSongCrossRef(obj: PlaylistSongCrossRef): Boolean
    suspend fun insert(playlist: Playlist)
    suspend fun deletePlaylist(playlist: Playlist)
    suspend fun updatePlaylist(playlist: Playlist)
}