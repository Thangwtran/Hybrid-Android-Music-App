package com.example.hybridmusicapp.data.repository.recent_song

import com.example.hybridmusicapp.data.model.recent.RecentSong
import com.example.hybridmusicapp.data.model.song.Song
import kotlinx.coroutines.flow.Flow

interface RecentSongRepository {
    val recentSongs: Flow<List<Song>> // or Song

    suspend fun insertAllRecentSong(vararg songs: RecentSong)
}