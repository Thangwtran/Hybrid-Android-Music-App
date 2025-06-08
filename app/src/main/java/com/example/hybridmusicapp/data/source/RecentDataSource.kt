package com.example.hybridmusicapp.data.source

import com.example.hybridmusicapp.data.model.recent.RecentNcs
import com.example.hybridmusicapp.data.model.recent.RecentSong
import com.example.hybridmusicapp.data.model.song.NCSong
import com.example.hybridmusicapp.data.model.song.Song
import kotlinx.coroutines.flow.Flow

// Local
interface RecentDataSource {
    fun getRecentSongs(): Flow<List<Song>>
    fun getRecentNcs(): Flow<List<NCSong>>
    suspend fun insert(vararg songs: RecentSong)
    suspend fun insert(vararg songs: RecentNcs)
}