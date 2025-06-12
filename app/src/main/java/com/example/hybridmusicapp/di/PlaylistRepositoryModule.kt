package com.example.hybridmusicapp.di

import com.example.hybridmusicapp.data.repository.playlist.PlaylistRepository
import com.example.hybridmusicapp.data.repository.playlist.PlaylistRepositoryImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
abstract class PlaylistRepositoryModule {

    @Binds
    abstract fun bindPlaylistRepository(repository: PlaylistRepositoryImp): PlaylistRepository
}