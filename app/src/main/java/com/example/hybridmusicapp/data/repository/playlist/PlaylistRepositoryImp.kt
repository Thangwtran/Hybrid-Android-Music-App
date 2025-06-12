package com.example.hybridmusicapp.data.repository.playlist

import com.example.hybridmusicapp.data.model.playlist.Playlist
import com.example.hybridmusicapp.data.model.playlist.PlaylistSongCrossRef
import com.example.hybridmusicapp.data.model.playlist.PlaylistWithSongs
import com.example.hybridmusicapp.data.source.PlaylistDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PlaylistRepositoryImp @Inject constructor(
    private val dataSource: PlaylistDataSource
) : PlaylistRepository {
    override fun getAllPlaylists(): Flow<List<Playlist>> {
        return dataSource.getAllPlaylist()
    }

    override fun getAllPlaylistWithSongs(): Flow<List<PlaylistWithSongs>> {
        return dataSource.getAllPlaylistWithSongs()
    }

    override suspend fun insertPlaylistSongCrossRef(obj: PlaylistSongCrossRef): Boolean {
        return dataSource.insertPlaylistSongCrossRef(obj)
    }

    override suspend fun getPlaylistWithSongByPlaylistId(id: Int): Flow<PlaylistWithSongs> {
        return dataSource.getPlaylistWithSongByPlaylistId(id)
    }

    override suspend fun insertPlaylist(playlist: Playlist) {
        dataSource.insert(playlist)
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        dataSource.deletePlaylist(playlist)
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        dataSource.updatePlaylist(playlist)
    }
}