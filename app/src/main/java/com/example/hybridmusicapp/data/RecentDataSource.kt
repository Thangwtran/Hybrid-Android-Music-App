package com.example.hybridmusicapp.data

import com.example.hybridmusicapp.data.model.recent.RecentSong
import com.example.hybridmusicapp.data.model.song.Song
import kotlinx.coroutines.flow.Flow

// Local
interface RecentDataSource {
    fun getRecentSongs(): Flow<List<Song>>
    suspend fun insert(vararg songs: RecentSong)
}