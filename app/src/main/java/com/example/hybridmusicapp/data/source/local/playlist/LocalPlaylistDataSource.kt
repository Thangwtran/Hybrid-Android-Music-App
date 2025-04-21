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

    override suspend fun delete(playlist: Playlist) {
        return playlistDao.delete(playlist)
    }

    override suspend fun update(playlist: Playlist) {
        return playlistDao.update(playlist)
    }

}