package com.example.hybridmusicapp.data.repository.artist

import com.example.hybridmusicapp.ResultCallback
import com.example.hybridmusicapp.data.model.artist.Artist
import com.example.hybridmusicapp.data.model.artist.ArtistList

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
        suspend fun loadRemoteArtists(result: Result<ArtistList>)
        suspend fun addArtistToFireStore(artists: List<Artist>)
        suspend fun getArtists(callback: ResultCallback<Result<List<Artist>>>)
        suspend fun getArtistFirebase(callback: ResultCallback<Result<List<Artist>>>)
    }
}