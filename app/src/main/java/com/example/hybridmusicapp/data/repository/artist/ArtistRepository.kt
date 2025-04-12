package com.example.hybridmusicapp.data.repository.artist

import com.example.hybridmusicapp.data.model.artist.Artist

interface ArtistRepository {
    interface Local{
        fun getLocalArtists(): List<Artist>
        // TODO: artist with song
        suspend fun insertArtists(artists: List<Artist>)
        // TODO: artist cross ref
        suspend fun deleteArtist(artist: Artist)
        suspend fun updateArtist(artist: Artist)
    }

    interface Remote{
        // TODO: get request 
    }
}