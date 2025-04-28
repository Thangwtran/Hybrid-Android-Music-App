package com.example.hybridmusicapp.data.source.local.playlist

import com.example.hybridmusicapp.data.source.PlaylistDataSource
import com.example.hybridmusicapp.data.model.playlist.Playlist
import kotlinx.coroutines.flow.Flow

class LocalPlaylistDataSource(
    private val playlistDao: PlaylistDao
): PlaylistDataSource {
    override fun getAllPlaylist(): Flow<List<Playlist>> {
        return playlistDao.playlists
    }

    override suspend fun insert(playlist: Playlist) {
        return playlistDao.insert(playlist)
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        TODO("Not yet implemented")
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        TODO("Not yet implemented")
    }


}