package com.example.hybridmusicapp.data.repository.recent_song

import com.example.hybridmusicapp.data.model.recent.RecentSong
import kotlinx.coroutines.flow.Flow

interface RecentSongRepository {
    val recentSongs: Flow<List<RecentSong>> // or Song

    suspend fun insertAllRecentSong(vararg songs: RecentSong)
}