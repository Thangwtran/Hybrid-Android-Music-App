package com.example.hybridmusicapp

import android.app.Application
import com.example.hybridmusicapp.data.repository.album.AlbumRepositoryImp
import com.example.hybridmusicapp.data.repository.artist.ArtistRepositoryImp
import com.example.hybridmusicapp.data.repository.song.NCSongRepositoryImp
import com.example.hybridmusicapp.data.repository.song.SongRepositoryImp
import com.example.hybridmusicapp.utils.InjectionUtils

class MusicApplication: Application(){
    lateinit var songRepository: SongRepositoryImp
    lateinit var albumRepository: AlbumRepositoryImp
    lateinit var artistRepository: ArtistRepositoryImp
    lateinit var ncsRepository: NCSongRepositoryImp

    override fun onCreate() {
        super.onCreate()
        setupViewModelComponents()
    }

    private fun setupViewModelComponents() {
        // song
        val localSongDataSource = InjectionUtils.provideLocalDataSource(applicationContext)
        songRepository = InjectionUtils.provideSongRepository(localSongDataSource)

        // album
        val localAlbumDataSource = InjectionUtils.provideAlbumLocalDataSource(applicationContext)
        albumRepository = InjectionUtils.provideAlbumRepository(localAlbumDataSource)

        // artist
        val localArtistDataSource = InjectionUtils.provideArtistLocalDataSource(applicationContext)
        artistRepository = InjectionUtils.provideArtistRepository(localArtistDataSource)

        // ncs
        val localNCSongDataSource = InjectionUtils.provideNCSongLocalDataSource(applicationContext)
        ncsRepository = InjectionUtils.provideNCSongRepository(localNCSongDataSource)


    }
}