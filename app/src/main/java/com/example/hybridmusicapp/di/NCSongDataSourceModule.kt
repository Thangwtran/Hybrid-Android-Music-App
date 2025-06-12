package com.example.hybridmusicapp.di

import com.example.hybridmusicapp.data.source.NCSongDataSource
import com.example.hybridmusicapp.data.source.local.ncsong.LocalNCSongDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class NCSongDataSourceModule {
    @Binds
    abstract fun bindNCSongDataSource(dataSource: LocalNCSongDataSource): NCSongDataSource
}