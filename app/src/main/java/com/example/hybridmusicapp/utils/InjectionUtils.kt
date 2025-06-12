package com.example.hybridmusicapp.utils

import android.content.Context
import com.example.hybridmusicapp.data.repository.album.AlbumRepositoryImp
import com.example.hybridmusicapp.data.repository.artist.ArtistRepositoryImp
import com.example.hybridmusicapp.data.repository.playlist.PlaylistRepositoryImp
import com.example.hybridmusicapp.data.repository.recent_song.RecentSongRepositoryImp
import com.example.hybridmusicapp.data.repository.search.SearchingRepositoryImp
import com.example.hybridmusicapp.data.repository.song.NCSongRepositoryImp
import com.example.hybridmusicapp.data.repository.song.SongRepositoryImp
import com.example.hybridmusicapp.data.source.AlbumDataSource
import com.example.hybridmusicapp.data.source.ArtistDataSource
import com.example.hybridmusicapp.data.source.PlaylistDataSource
import com.example.hybridmusicapp.data.source.SongDataSource
import com.example.hybridmusicapp.data.source.local.AppDatabase
import com.example.hybridmusicapp.data.source.local.album.LocalAlbumDataSource
import com.example.hybridmusicapp.data.source.local.artist.LocalArtistDataSource
import com.example.hybridmusicapp.data.source.local.ncsong.LocalNCSongDataSource
import com.example.hybridmusicapp.data.source.local.playlist.LocalPlaylistDataSource
import com.example.hybridmusicapp.data.source.local.recent_song.RecentSongDataSource
import com.example.hybridmusicapp.data.source.local.searching.LocalSearchDataSource
import com.example.hybridmusicapp.data.source.local.song.LocalSongDataSource

object InjectionUtils {

//    // Song
//    fun provideLocalDataSource(context: Context): SongDataSource.Local { // local song data source
//        val database = AppDatabase.getInstance(context)
//        return LocalSongDataSource(database.songDao())
//    }
//
//    fun provideSongRepository(datasource: SongDataSource.Local): SongRepositoryImp { // song repository
//        return SongRepositoryImp(localDataSource = datasource)
//    }
//
//    // Album
//    fun provideAlbumLocalDataSource(context: Context): AlbumDataSource.Local { // local album data source
//        val database = AppDatabase.getInstance(context)
//        return LocalAlbumDataSource(database.albumDao())
//    }
//
//    fun provideAlbumRepository(datasource: AlbumDataSource.Local): AlbumRepositoryImp { // album repository
//        return AlbumRepositoryImp(localAlbumDataSource = datasource)
//    }
//
//    // Playlist
//    fun providePlaylistLocalDataSource(context: Context): PlaylistDataSource{
//        val database = AppDatabase.getInstance(context)
//        return LocalPlaylistDataSource(database.playlistDao())
//    }
//
//    fun providePlaylistRepository(datasource: PlaylistDataSource): PlaylistRepositoryImp {
//        return PlaylistRepositoryImp(dataSource = datasource)
//    }
//
//    // Artist
//    fun provideArtistLocalDataSource(context: Context): ArtistDataSource.Local { // local artist data source
//        val database = AppDatabase.getInstance(context)
//        return LocalArtistDataSource(database.artistDao())
//    }
//
//    fun provideArtistRepository(datasource: ArtistDataSource.Local): ArtistRepositoryImp { // artist repository
//        return ArtistRepositoryImp(localDataSource = datasource)
//    }
//
//    // NCS
//    fun provideNCSongLocalDataSource(context: Context): LocalNCSongDataSource  { // local artist data source
//        val database = AppDatabase.getInstance(context)
//        return LocalNCSongDataSource(database.ncSongDao())
//    }
//
//    fun provideNCSongRepository(datasource: LocalNCSongDataSource): NCSongRepositoryImp { // artist repository
//        return NCSongRepositoryImp(localDataSource = datasource)
//    }
//
//    // Recent Song
//    fun provideRecentSongDataSource(context: Context): RecentSongDataSource {
//        val database = AppDatabase.getInstance(context)
//        return RecentSongDataSource(database.recentSongDao())
//    }
//
//    fun provideRecentSongRepository(datasource: RecentSongDataSource): RecentSongRepositoryImp {
//        return RecentSongRepositoryImp(recentSongDataSource = datasource)
//    }
//
//    // Searching
//    fun provideSearchingDataSource(context: Context): LocalSearchDataSource{
//        val database = AppDatabase.getInstance(context)
//        return LocalSearchDataSource(database.searchingDao())
//    }
//
//    fun provideSearchingRepository(datasource: LocalSearchDataSource): SearchingRepositoryImp{
//        return SearchingRepositoryImp(searchingDataSource = datasource)
//    }
}