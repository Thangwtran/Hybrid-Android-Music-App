package com.example.hybridmusicapp.data.source.local.artist

import com.example.hybridmusicapp.data.ArtistDataSource

class LocalArtistDataSource (
    private val artistDao: ArtistDao
): ArtistDataSource.Local{

}