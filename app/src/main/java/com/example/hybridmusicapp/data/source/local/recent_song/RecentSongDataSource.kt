package com.example.hybridmusicapp.data.source.local.recent_song

import com.example.hybridmusicapp.data.source.RecentDataSource
import com.example.hybridmusicapp.data.model.recent.RecentSong
import com.example.hybridmusicapp.data.model.song.Song
import kotlinx.coroutines.flow.Flow

class RecentSongDataSource(
    private val recentSongDao: RecentSongDao
) : RecentDataSource {
    override fun getRecentSongs(): Flow<List<Song>> {
        return recentSongDao.recentSongs
    }

    override suspend fun insert(vararg songs: RecentSong) {
        return recentSongDao.insert(*songs)
    }

}