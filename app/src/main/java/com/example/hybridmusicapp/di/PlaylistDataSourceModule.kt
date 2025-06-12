package com.example.hybridmusicapp.di

import com.example.hybridmusicapp.data.source.PlaylistDataSource
import com.example.hybridmusicapp.data.source.local.playlist.LocalPlaylistDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
abstract class PlaylistDataSourceModule {

    @Binds
    abstract fun bindPlaylistDataSource(dataSource: LocalPlaylistDataSource): PlaylistDataSource
}