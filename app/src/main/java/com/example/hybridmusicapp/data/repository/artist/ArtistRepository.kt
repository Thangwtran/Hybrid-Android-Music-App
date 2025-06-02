package com.example.hybridmusicapp.data.repository.artist

import com.example.hybridmusicapp.ResultCallback
import com.example.hybridmusicapp.data.model.artist.Artist
import com.example.hybridmusicapp.data.model.artist.ArtistList
import kotlinx.coroutines.flow.Flow
import  com.example.hybridmusicapp.data.source.remote.Result
interface ArtistRepository {
    interface Local{
        fun getLocalArtists(): Flow<List<Artist>>
        // TODO: artist with song
        suspend fun insertArtists(vararg artist: Artist)
        // TODO: artist cross ref
        suspend fun deleteArtist(artist: Artist)
        suspend fun updateArtist(artist: Artist)
    }

    interface Remote{
        // TODO: get request
        suspend fun loadRemoteArtists(): Result<ArtistList>
        suspend fun addArtistToFireStore(artists: List<Artist>)
        suspend fun getTop20ArtistFirebase(callback: ResultCallback<Result<List<Artist>>>)
        suspend fun getArtistFirebase(callback: ResultCallback<Result<List<Artist>>>)
    }
}