package com.example.hybridmusicapp.di

import com.example.hybridmusicapp.data.source.SongDataSource
import com.example.hybridmusicapp.data.source.local.song.LocalSongDataSource
import com.example.hybridmusicapp.data.source.remote.RemoteSongDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class SongDataSourceModule {

    @Binds
    abstract fun bindRemoteDataSource(remoteDataSource: RemoteSongDataSource): SongDataSource.Remote

    @Binds
    abstract fun bindLocalDataSource(localDataSource: LocalSongDataSource): SongDataSource.Local
}