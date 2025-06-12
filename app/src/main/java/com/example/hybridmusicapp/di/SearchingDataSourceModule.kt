package com.example.hybridmusicapp.di

import com.example.hybridmusicapp.data.source.SearchingDataSource
import com.example.hybridmusicapp.data.source.local.searching.LocalSearchDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class SearchingDataSourceModule {

    @Binds
    abstract fun bindSearchingDataSource(searchingDataSource: LocalSearchDataSource): SearchingDataSource

}