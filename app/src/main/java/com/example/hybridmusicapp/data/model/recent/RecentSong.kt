package com.example.hybridmusicapp.data.model.recent

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.example.hybridmusicapp.data.model.song.NCSong
import com.example.hybridmusicapp.data.model.song.Song
import com.example.hybridmusicapp.ui.home.adapter.RecommendedSong
import java.util.Date


@Entity(tableName = "recent_songs")
data class RecentSong(
    @ColumnInfo(name = "play_at")
    var playAt: Date = Date()
) : Song() {// Song()
    class Builder(song: Song) {
        init {
            sRecentSong = RecentSong()
            sRecentSong.id = song.id
            sRecentSong.title = song.title
            sRecentSong.album = song.album
            sRecentSong.artist = song.artist
            sRecentSong.duration = song.duration
            sRecentSong.source = song.source
            sRecentSong.image = song.image
            sRecentSong.isFavorite = song.isFavorite
            sRecentSong.counter = song.counter
            sRecentSong.replay = song.replay
            sRecentSong.playAt = Date()
//            sRecentSong.song = song
//            sRecentSong.ncSong = ncSong
        }

        fun playAt(value: Date): Builder {
            sRecentSong.playAt = value
            return this
        }

        fun build(): RecentSong {
            return sRecentSong
        }

        companion object {
            private lateinit var sRecentSong: RecentSong
        }
    }
}