package com.example.hybridmusicapp.data.repository.recent_song

import com.example.hybridmusicapp.data.model.recent.RecentNcs
import com.example.hybridmusicapp.data.model.recent.RecentSong
import com.example.hybridmusicapp.data.model.song.NCSong
import com.example.hybridmusicapp.data.model.song.Song
import com.example.hybridmusicapp.data.source.RecentDataSource
import com.example.hybridmusicapp.data.source.local.recent_song.RecentSongDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RecentSongRepositoryImp @Inject constructor(
    private val recentSongDataSource: RecentDataSource,
): RecentSongRepository {
    override val recentSongs: Flow<List<Song>>
        get() = recentSongDataSource.getRecentSongs()

    override val recentNcsSongs: Flow<List<NCSong>>
        get() = recentSongDataSource.getRecentNcs()

    override suspend fun insertAllRecentSong(vararg songs: RecentSong) {
        recentSongDataSource.insert(*songs)
    }

    override suspend fun insertAllRecentNcsSong(vararg songs: RecentNcs) {
        recentSongDataSource.insert(*songs)
    }
}