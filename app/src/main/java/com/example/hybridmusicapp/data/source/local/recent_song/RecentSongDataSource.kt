package com.example.hybridmusicapp.data.source.local.recent_song

import com.example.hybridmusicapp.data.model.recent.RecentNcs
import com.example.hybridmusicapp.data.source.RecentDataSource
import com.example.hybridmusicapp.data.model.recent.RecentSong
import com.example.hybridmusicapp.data.model.song.NCSong
import com.example.hybridmusicapp.data.model.song.Song
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RecentSongDataSource @Inject constructor(
    private val recentSongDao: RecentSongDao
) : RecentDataSource {
    override fun getRecentSongs(): Flow<List<Song>> {
        return recentSongDao.recentSongs
    }

    override fun getRecentNcs(): Flow<List<NCSong>> {
        return recentSongDao.recentNcs
    }

    override suspend fun insert(vararg songs: RecentSong) {
        return recentSongDao.insert(*songs)
    }

    override suspend fun insert(vararg ncsSongs: RecentNcs) {
        return recentSongDao.insert(*ncsSongs)
    }

}