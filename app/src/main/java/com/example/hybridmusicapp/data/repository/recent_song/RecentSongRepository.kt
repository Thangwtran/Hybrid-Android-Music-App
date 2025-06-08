package com.example.hybridmusicapp.data.repository.recent_song

import com.example.hybridmusicapp.data.model.recent.RecentNcs
import com.example.hybridmusicapp.data.model.recent.RecentSong
import com.example.hybridmusicapp.data.model.song.NCSong
import com.example.hybridmusicapp.data.model.song.Song
import kotlinx.coroutines.flow.Flow

interface RecentSongRepository {
    val recentSongs: Flow<List<Song>> // or Song

    val recentNcsSongs: Flow<List<NCSong>>

    suspend fun insertAllRecentSong(vararg songs: RecentSong)

    suspend fun insertAllRecentNcsSong(vararg songs: RecentNcs)

}