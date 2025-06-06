package com.example.hybridmusicapp.data.repository.recent_song

import com.example.hybridmusicapp.data.model.recent.RecentSong
import com.example.hybridmusicapp.data.model.song.Song
import com.example.hybridmusicapp.data.source.RecentDataSource
import com.example.hybridmusicapp.data.source.local.recent_song.RecentSongDataSource
import kotlinx.coroutines.flow.Flow

class RecentSongRepositoryImp(
    private val recentSongDataSource: RecentDataSource,
): RecentSongRepository {
    override val recentSongs: Flow<List<Song>>
        get() = recentSongDataSource.getRecentSongs()

    override suspend fun insertAllRecentSong(vararg songs: RecentSong) {
        recentSongDataSource.insert(*songs)
    }
}