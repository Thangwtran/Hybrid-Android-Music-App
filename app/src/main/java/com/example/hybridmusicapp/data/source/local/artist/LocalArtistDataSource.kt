package com.example.hybridmusicapp.data.source.local.artist

import com.example.hybridmusicapp.data.ArtistDataSource
import com.example.hybridmusicapp.data.model.artist.Artist
import kotlinx.coroutines.flow.Flow

class LocalArtistDataSource (
    private val artistDao: ArtistDao
): ArtistDataSource.Local{
    override fun getArtists(): Flow<List<Artist>> {
        return artistDao.artists
    }

    override suspend fun insertArtist(vararg artist: Artist) {
        artistDao.insertArtist(*artist)
    }

    override suspend fun deleteArtist(artist: Artist) {
        artistDao.deleteArtist(artist)
    }

    override suspend fun updateArtist(artist: Artist) {
        artistDao.updateArtist(artist)
    }

}