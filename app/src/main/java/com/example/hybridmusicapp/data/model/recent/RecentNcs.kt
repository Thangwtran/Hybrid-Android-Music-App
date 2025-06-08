package com.example.hybridmusicapp.data.model.recent

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.example.hybridmusicapp.data.model.song.NCSong
import com.example.hybridmusicapp.data.model.song.Song
import java.util.Date

@Entity(tableName = "recent_ncs")
data class RecentNcs(
    @ColumnInfo(name = "play_at")
    var playAt: Date = Date()
): NCSong(){
    class Builder(ncs: NCSong) {
        init {
            sRecentSong = RecentNcs()
            sRecentSong.id = ncs.id
            sRecentSong.ncsName = ncs.ncsName
            sRecentSong.artist = ncs.artist
            sRecentSong.audioRes = ncs.audioRes
            sRecentSong.imageRes = ncs.imageRes
            sRecentSong.isFavourite = ncs.isFavourite
            sRecentSong.genre = ncs.genre
            sRecentSong.numOfInterest = ncs.numOfInterest
            sRecentSong.playAt = Date()
//            sRecentSong.song = song
//            sRecentSong.ncSong = ncSong
        }

        fun playAt(value: Date): RecentNcs.Builder {
            sRecentSong.playAt = value
            return this
        }

        fun build(): RecentNcs {
            return sRecentSong
        }

        companion object {
            private lateinit var sRecentSong: RecentNcs
        }
    }
}