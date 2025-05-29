package com.example.hybridmusicapp.data.source.local.playlist

import com.example.hybridmusicapp.data.source.PlaylistDataSource
import com.example.hybridmusicapp.data.model.playlist.Playlist
import com.example.hybridmusicapp.data.model.playlist.PlaylistSongCrossRef
import com.example.hybridmusicapp.data.model.playlist.PlaylistWithSongs
import kotlinx.coroutines.flow.Flow

class LocalPlaylistDataSource(
    private val playlistDao: PlaylistDao
): PlaylistDataSource {
    override fun getAllPlaylist(): Flow<List<Playlist>> {
        return playlistDao.playlists
    }

    override fun getAllPlaylistWithSongs(): Flow<List<PlaylistWithSongs>> {
        return playlistDao.getAllPlaylistWithSongs()
    }

    override fun getPlaylistWithSongByPlaylistId(id: Int): Flow<PlaylistWithSongs> {
        return playlistDao.getPlaylistWithSongByPlaylistId(id)
    }

    override suspend fun insertPlaylistSongCrossRef(obj: PlaylistSongCrossRef): Boolean {
        return playlistDao.insertPlaylistSongCrossRef(obj) != -1L
    }

    override suspend fun insert(playlist: Playlist) {
        return playlistDao.insert(playlist)
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        playlistDao.delete(playlist)
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        playlistDao.update(playlist)
    }
}