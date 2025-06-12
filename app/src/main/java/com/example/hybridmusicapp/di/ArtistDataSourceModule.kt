package com.example.hybridmusicapp.di

import com.example.hybridmusicapp.data.source.ArtistDataSource
import com.example.hybridmusicapp.data.source.local.artist.LocalArtistDataSource
import com.example.hybridmusicapp.data.source.remote.RemoteArtistDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
abstract class ArtistDataSourceModule {

    @Binds
    abstract fun bindLocalArtistDataSource(localDataSource: LocalArtistDataSource): ArtistDataSource.Local

    @Binds
    abstract fun bindRemoteArtistDataSource(remoteDataSource: RemoteArtistDataSource): ArtistDataSource.Remote


}