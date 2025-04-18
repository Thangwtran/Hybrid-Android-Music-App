package com.example.hybridmusicapp.data.source.local.searching

import com.example.hybridmusicapp.data.SearchingDataSource
import com.example.hybridmusicapp.data.model.history.HistorySearchedKey
import com.example.hybridmusicapp.data.model.history.HistorySearchedSong
import com.example.hybridmusicapp.data.model.song.Song
import kotlinx.coroutines.flow.Flow

class LocalSearchDataSource(
    private val searchingDao: SearchingDao
):SearchingDataSource{
    override fun getAllHistorySearchKeys(): Flow<List<HistorySearchedKey>> {
        return searchingDao.allKeys
    }

    override fun getAllHistorySearchSongs(): Flow<List<HistorySearchedSong>> {
        return searchingDao.allSongs
    }

    override fun searchSongs(key: String): Flow<List<Song>> {
        return searchingDao.search(key)
    }

    override suspend fun insertKey(vararg keys: HistorySearchedKey) {
        return searchingDao.insertKey(*keys)
    }

    override suspend fun insertSong(vararg songs: HistorySearchedSong) {
        return searchingDao.insertSong(*songs)
    }

    override suspend fun clearSearchKeys() {
        return searchingDao.clearSearchKeys()
    }

    override suspend fun clearSearchSongs() {
        return searchingDao.clearSearchSongs()
    }

}