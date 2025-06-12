package com.example.hybridmusicapp.di

import android.content.Context
import androidx.room.Room
import com.example.hybridmusicapp.data.source.local.AppDatabase
import com.example.hybridmusicapp.data.source.local.album.AlbumDao
import com.example.hybridmusicapp.data.source.local.artist.ArtistDao
import com.example.hybridmusicapp.data.source.local.ncsong.NCSongDao
import com.example.hybridmusicapp.data.source.local.playlist.PlaylistDao
import com.example.hybridmusicapp.data.source.local.recent_song.RecentSongDao
import com.example.hybridmusicapp.data.source.local.searching.SearchingDao
import com.example.hybridmusicapp.data.source.local.song.SongDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room
            .databaseBuilder(context, AppDatabase::class.java, "music.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideSongDao(appDatabase: AppDatabase): SongDao {
        return appDatabase.songDao()
    }

    @Provides
    fun provideAlbumDao(appDatabase: AppDatabase): AlbumDao {
        return appDatabase.albumDao()
    }

    @Provides
    fun provideArtistDao(appDatabase: AppDatabase): ArtistDao {
        return appDatabase.artistDao()
    }

    @Provides
    fun provideNCSongDao(appDatabase: AppDatabase): NCSongDao {
        return appDatabase.ncSongDao()
    }

    @Provides
    fun providePlaylistDao(appDatabase: AppDatabase): PlaylistDao {
        return appDatabase.playlistDao()
    }

    @Provides
    fun provideRecentSongDao(appDatabase: AppDatabase): RecentSongDao {
        return appDatabase.recentSongDao()
    }

    @Provides
    fun searchingDao(appDatabase: AppDatabase): SearchingDao {
        return appDatabase.searchingDao()
    }
}