package com.example.hybridmusicapp.di

import com.example.hybridmusicapp.data.repository.recent_song.RecentSongRepositoryImp
import com.example.hybridmusicapp.data.repository.search.SearchingRepositoryImp
import com.example.hybridmusicapp.data.repository.song.NCSongRepositoryImp
import com.example.hybridmusicapp.data.repository.song.SongRepositoryImp
import com.example.hybridmusicapp.ui.viewmodel.NowPlayingViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NowPlayingViewModelModule {

    @Provides
    @Singleton
    fun provideNowPlayingViewModel(
        songRepository: SongRepositoryImp,
        ncsSongRepository: NCSongRepositoryImp,
        searchRepository: SearchingRepositoryImp,
        recentSongRepository: RecentSongRepositoryImp
    ): NowPlayingViewModel{
        return NowPlayingViewModel(
            songRepository,
            ncsSongRepository,
            searchRepository,
            recentSongRepository
        )
    }
}