package com.example.hybridmusicapp.data.source.local.searching

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.hybridmusicapp.data.model.history.HistorySearchedKey
import com.example.hybridmusicapp.data.model.history.HistorySearchedSong
import com.example.hybridmusicapp.data.model.song.Song
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchingDao {
    @get:Query("SELECT * FROM history_searched_keys ORDER BY created_at DESC LIMIT 50")
    val allKeys: Flow<List<HistorySearchedKey>>

    @get:Query("SELECT * FROM history_searched_songs ORDER BY selected_at DESC LIMIT 50")
    val allSongs: Flow<List<HistorySearchedSong>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertKey(vararg keys: HistorySearchedKey)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSong(vararg songs: HistorySearchedSong)

    @Query("""
    SELECT * FROM songs 
    WHERE title LIKE '%' || :key || '%'  COLLATE NOCASE
       OR artist LIKE '%' || :key || '%'  COLLATE NOCASE
    ORDER BY title
""")
    fun search(key:String): Flow<List<Song>>

    @Query("DELETE FROM history_searched_keys")
    fun clearSearchKeys()

    @Query("DELETE FROM history_searched_songs")
    fun clearSearchSongs()
}