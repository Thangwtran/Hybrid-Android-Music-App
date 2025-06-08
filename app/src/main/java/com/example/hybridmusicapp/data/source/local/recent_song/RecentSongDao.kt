package com.example.hybridmusicapp.data.source.local.recent_song

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.hybridmusicapp.data.model.recent.RecentNcs
import com.example.hybridmusicapp.data.model.recent.RecentSong
import com.example.hybridmusicapp.data.model.song.NCSong
import com.example.hybridmusicapp.data.model.song.Song
import kotlinx.coroutines.flow.Flow

@Dao
interface RecentSongDao {
    @get:Query("SELECT * FROM recent_songs ORDER BY play_at DESC LIMIT 30")
    val recentSongs: Flow<List<Song>>

    @get:Query("SELECT * FROM recent_ncs ORDER BY play_at DESC LIMIT 30")
    val recentNcs: Flow<List<NCSong>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg songs: RecentSong)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg songs: RecentNcs)

}