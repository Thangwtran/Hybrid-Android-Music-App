package com.example.hybridmusicapp.utils

import android.content.Context
import com.example.hybridmusicapp.data.repository.album.AlbumRepositoryImp
import com.example.hybridmusicapp.data.repository.artist.ArtistRepositoryImp
import com.example.hybridmusicapp.data.repository.song.NCSongRepositoryImp
import com.example.hybridmusicapp.data.repository.song.SongRepositoryImp
import com.example.hybridmusicapp.data.source.AlbumDataSource
import com.example.hybridmusicapp.data.source.ArtistDataSource
import com.example.hybridmusicapp.data.source.SongDataSource
import com.example.hybridmusicapp.data.source.local.AppDatabase
import com.example.hybridmusicapp.data.source.local.album.LocalAlbumDataSource
import com.example.hybridmusicapp.data.source.local.artist.LocalArtistDataSource
import com.example.hybridmusicapp.data.source.local.ncsong.LocalNCSongDataSource
import com.example.hybridmusicapp.data.source.local.recent_song.RecentSongDataSource
import com.example.hybridmusicapp.data.source.local.song.LocalSongDataSource

object InjectionUtils {

    // Song
    fun provideLocalDataSource(context: Context): SongDataSource.Local { // local song data source
        val database = AppDatabase.getInstance(context)
        return LocalSongDataSource(database.songDao())
    }

    fun provideSongRepository(datasource: SongDataSource.Local): SongRepositoryImp { // song repository
        return SongRepositoryImp(localDataSource = datasource)
    }

    // Album
    fun provideAlbumLocalDataSource(context: Context): AlbumDataSource.Local { // local album data source
        val database = AppDatabase.getInstance(context)
        return LocalAlbumDataSource(database.albumDao())
    }

    fun provideAlbumRepository(datasource: AlbumDataSource.Local): AlbumRepositoryImp { // album repository
        return AlbumRepositoryImp(localAlbumDataSource = datasource)
    }

    // Artist
    fun provideArtistLocalDataSource(context: Context): ArtistDataSource.Local { // local artist data source
        val database = AppDatabase.getInstance(context)
        return LocalArtistDataSource(database.artistDao())
    }

    // NCS
    fun provideNCSongLocalDataSource(context: Context): LocalNCSongDataSource  { // local artist data source
        val database = AppDatabase.getInstance(context)
        return LocalNCSongDataSource(database.ncSongDao())
    }
    fun provideNCSongRepository(datasource: LocalNCSongDataSource): NCSongRepositoryImp { // artist repository
        return NCSongRepositoryImp(localDataSource = datasource)
    }

    fun provideArtistRepository(datasource: ArtistDataSource.Local): ArtistRepositoryImp { // artist repository
        return ArtistRepositoryImp(localDataSource = datasource)
    }


    // Recent Song
    fun provideRecentSongDataSource(context: Context): RecentSongDataSource {
        val database = AppDatabase.getInstance(context)
        return RecentSongDataSource(database.recentSongDao())
    }

    // TODO: Recent repository

}