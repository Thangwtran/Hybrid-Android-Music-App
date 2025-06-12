package com.example.hybridmusicapp.di

import com.example.hybridmusicapp.data.source.AlbumDataSource
import com.example.hybridmusicapp.data.source.local.album.LocalAlbumDataSource
import com.example.hybridmusicapp.data.source.remote.RemoteAlbumDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AlbumDataSourceModule {

    @Binds
    abstract fun bindLocalAlbumDataSource(localDataSource: LocalAlbumDataSource): AlbumDataSource.Local

    @Binds
    abstract fun bindRemoteAlbumDataSource(remoteDataSource: RemoteAlbumDataSource): AlbumDataSource.Remote

}
