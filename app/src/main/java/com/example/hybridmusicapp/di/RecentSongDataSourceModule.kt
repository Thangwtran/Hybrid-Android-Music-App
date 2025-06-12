package com.example.hybridmusicapp.di

import com.example.hybridmusicapp.data.source.RecentDataSource
import com.example.hybridmusicapp.data.source.local.recent_song.RecentSongDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RecentSongDataSourceModule {

    @Binds
    abstract fun bindRecentSongDataSource(recentSongDataSource: RecentSongDataSource): RecentDataSource
}