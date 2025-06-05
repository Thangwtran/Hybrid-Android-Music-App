package com.example.hybridmusicapp.data.source.local.artist

import com.example.hybridmusicapp.data.source.ArtistDataSource
import com.example.hybridmusicapp.data.model.artist.Artist
import com.example.hybridmusicapp.data.model.artist.ArtistSongCrossRef
import com.example.hybridmusicapp.data.model.artist.ArtistWithSongs
import kotlinx.coroutines.flow.Flow

class LocalArtistDataSource (
    private val artistDao: ArtistDao
): ArtistDataSource.Local{
    override fun getArtists(): Flow<List<Artist>> {
        return artistDao.artists
    }

    override suspend fun getArtistWithSongs(artistId: Int): ArtistWithSongs {
        return artistDao.getArtistWithSongs(artistId)
    }

    override suspend fun insertArtist(vararg artist: Artist) {
        artistDao.insertArtist(*artist)
    }

    override suspend fun insertArtistSongCrossRef(vararg values: ArtistSongCrossRef) {
        artistDao.insertArtistSongCrossRef(*values)
    }

    override suspend fun deleteArtist(artist: Artist) {
        artistDao.deleteArtist(artist)
    }

    override suspend fun updateArtist(artist: Artist) {
        artistDao.updateArtist(artist)
    }

}