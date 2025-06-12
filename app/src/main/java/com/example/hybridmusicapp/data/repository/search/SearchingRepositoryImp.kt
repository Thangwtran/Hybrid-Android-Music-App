package com.example.hybridmusicapp.data.repository.search

import com.example.hybridmusicapp.data.model.history.HistorySearchedKey
import com.example.hybridmusicapp.data.model.history.HistorySearchedSong
import com.example.hybridmusicapp.data.model.song.Song
import com.example.hybridmusicapp.data.source.SearchingDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchingRepositoryImp @Inject constructor(
    private val searchingDataSource: SearchingDataSource
): SearchRepository{

    override val historySearchedKeys: Flow<List<HistorySearchedKey>>
        get() = searchingDataSource.getAllHistorySearchKeys()

    override val historySearchedSongs: Flow<List<HistorySearchedSong>>
        get() = searchingDataSource.getAllHistorySearchSongs()

    override suspend fun insertKey(vararg keys: HistorySearchedKey) {
        searchingDataSource.insertKey(*keys)
    }

    override suspend fun insertSong(vararg songs: HistorySearchedSong) {
        searchingDataSource.insertSong(*songs)
    }

    override suspend fun search(key: String): Flow<List<Song>> {
        return searchingDataSource.searchSongs(key)
    }

    override suspend fun clearSearchKeys() {
        searchingDataSource.clearSearchKeys()
    }

    override suspend fun clearSearchSongs() {
        searchingDataSource.clearSearchSongs()
    }
}