package com.example.hybridmusicapp.data.source.local

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.hybridmusicapp.data.model.album.Album
import com.example.hybridmusicapp.data.model.album.AlbumSongCrossRef
import com.example.hybridmusicapp.data.model.artist.Artist
import com.example.hybridmusicapp.data.model.artist.ArtistSongCrossRef
import com.example.hybridmusicapp.data.model.history.HistorySearchedKey
import com.example.hybridmusicapp.data.model.history.HistorySearchedSong
import com.example.hybridmusicapp.data.model.playlist.Playlist
import com.example.hybridmusicapp.data.model.playlist.PlaylistSongCrossRef
import com.example.hybridmusicapp.data.model.recent.RecentNcs
import com.example.hybridmusicapp.data.model.recent.RecentSong
import com.example.hybridmusicapp.data.model.song.NCSong
import com.example.hybridmusicapp.data.model.song.Song
import com.example.hybridmusicapp.data.source.local.album.AlbumDao
import com.example.hybridmusicapp.data.source.local.artist.ArtistDao
import com.example.hybridmusicapp.data.source.local.ncsong.NCSongDao
import com.example.hybridmusicapp.data.source.local.playlist.PlaylistDao
import com.example.hybridmusicapp.data.source.local.recent_song.RecentSongDao
import com.example.hybridmusicapp.data.source.local.searching.SearchingDao
import com.example.hybridmusicapp.data.source.local.song.SongDao

@Database(
    entities = [
        Song::class,
        NCSong::class,
        Album::class,
        AlbumSongCrossRef::class,
        Artist::class,
        ArtistSongCrossRef::class,
        Playlist::class,
        PlaylistSongCrossRef::class,
        RecentSong::class,
        RecentNcs::class,
        HistorySearchedKey::class,
        HistorySearchedSong::class
    ],
    version = 3,
//    autoMigrations = [
//        AutoMigration(from = 2, to =4)
//    ],
    exportSchema = true
)
@TypeConverters(value = [DateConverter::class]) // convert Date to Long
abstract class AppDatabase : RoomDatabase() {
    abstract fun songDao(): SongDao
    abstract fun albumDao(): AlbumDao
    abstract fun playlistDao(): PlaylistDao
    abstract fun recentSongDao(): RecentSongDao
    abstract fun searchingDao(): SearchingDao
    abstract fun artistDao(): ArtistDao
    abstract fun ncSongDao(): NCSongDao

//    /**
//     * AppDatabase is a singleton
//     */
//    companion object {
//        @Volatile
//        private var _instance: AppDatabase? = null
//
//        fun getInstance(context: Context): AppDatabase {
//            if (_instance == null) {
//                synchronized(AppDatabase::class.java) {
//                    if (_instance == null) {
//                        _instance = databaseBuilder(
//                            context.applicationContext,
//                            AppDatabase::class.java,
//                            "music.db"
//                        ).fallbackToDestructiveMigration() // Delete all data when upgrade version
//                            .build()
//                    }
//                }
//            }
//            return _instance!!
//        }
//    }
}