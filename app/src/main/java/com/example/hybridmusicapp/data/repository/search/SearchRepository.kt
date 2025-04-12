package com.example.hybridmusicapp.data.repository.search

import com.example.hybridmusicapp.data.model.history.HistorySearchedKey
import com.example.hybridmusicapp.data.model.history.HistorySearchedSong
import com.example.hybridmusicapp.data.model.song.Song
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    val historySearchedKeys: Flow<List<HistorySearchedKey>>
    val historySearchedSongs: Flow<List<HistorySearchedSong>>
    suspend fun insertKey(vararg keys: HistorySearchedKey)
    suspend fun insertSong(vararg songs: HistorySearchedSong)

    suspend fun search(key: String): Flow<List<Song>>
    suspend fun clearSearchKeys()
    suspend fun clearSearchSongs()
}