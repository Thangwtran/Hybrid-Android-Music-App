package com.example.hybridmusicapp.di

import com.example.hybridmusicapp.data.repository.search.SearchRepository
import com.example.hybridmusicapp.data.repository.search.SearchingRepositoryImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
abstract class SearchingRepositoryModule {

    @Binds
    abstract fun bindSearchingRepository(searchingRepository: SearchingRepositoryImp): SearchRepository

}