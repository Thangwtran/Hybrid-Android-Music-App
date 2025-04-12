package com.example.hybridmusicapp.data.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import com.example.hybridmusicapp.data.model.album.Album
import com.example.hybridmusicapp.data.model.artist.Artist
import com.example.hybridmusicapp.data.model.history.HistorySearchedKey
import com.example.hybridmusicapp.data.model.history.HistorySearchedSong
import com.example.hybridmusicapp.data.model.playlist.Playlist
import com.example.hybridmusicapp.data.model.recent.RecentSong
import com.example.hybridmusicapp.data.model.song.Song
import com.example.hybridmusicapp.data.source.local.album.AlbumDao
import com.example.hybridmusicapp.data.source.local.artist.ArtistDao
import com.example.hybridmusicapp.data.source.local.playlist.PlaylistDao
import com.example.hybridmusicapp.data.source.local.recent_song.RecentSongDao
import com.example.hybridmusicapp.data.source.local.searching.SearchingDao
import com.example.hybridmusicapp.data.source.local.song.SongDao

@Database(
    entities = [
        Song::class,
        Album::class,
        Artist::class,
        Playlist::class,
        RecentSong::class,
        HistorySearchedKey::class,
        HistorySearchedSong::class
    ],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun songDao(): SongDao
    abstract fun albumDao(): AlbumDao
    abstract fun playlistDao(): PlaylistDao
    abstract fun recentSongDao(): RecentSongDao
    abstract fun searchingDao(): SearchingDao
    abstract fun artistDao(): ArtistDao

    /**
     * AppDatabase is a singleton
     */
    companion object {
        private var _instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            if (_instance == null) {
                synchronized(AppDatabase::class.java) {
                    if (_instance == null) {
                        _instance = databaseBuilder(
                            context.applicationContext,
                            AppDatabase::class.java,
                            "music.db"
                        ).build()
                    }
                }
            }
            return _instance!!
        }
    }
}