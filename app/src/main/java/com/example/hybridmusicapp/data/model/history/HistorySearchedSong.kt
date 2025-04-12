package com.example.hybridmusicapp.data.model.history

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.example.hybridmusicapp.data.model.song.Song
import java.util.Date

@Suppress("unused")
@Entity(tableName = "history_searched_songs")
data class HistorySearchedSong(
    @ColumnInfo(name = "selected_at")
    var selectedAt: Date? = null
) : Song()