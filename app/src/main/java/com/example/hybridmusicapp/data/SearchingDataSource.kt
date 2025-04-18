package com.example.hybridmusicapp.data

import com.example.hybridmusicapp.data.model.history.HistorySearchedKey
import com.example.hybridmusicapp.data.model.history.HistorySearchedSong
import com.example.hybridmusicapp.data.model.song.Song
import kotlinx.coroutines.flow.Flow


// Local
interface SearchingDataSource {
    fun getAllHistorySearchKeys(): Flow<List<HistorySearchedKey>>
    fun getAllHistorySearchSongs(): Flow<List<HistorySearchedSong>>
    fun searchSongs(key: String): Flow<List<Song>>
    suspend fun insertKey(vararg keys: HistorySearchedKey)
    suspend fun insertSong(vararg songs: HistorySearchedSong)
    suspend fun clearSearchKeys()
    suspend fun clearSearchSongs()
}