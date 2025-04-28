package com.example.hybridmusicapp.data.source


import com.example.hybridmusicapp.ResultCallback
import com.example.hybridmusicapp.data.model.artist.Artist
import kotlinx.coroutines.flow.Flow

interface ArtistDataSource {
    interface Local{
        fun getArtists(): Flow<List<Artist>>
        suspend fun insertArtist(vararg artist: Artist)
        suspend fun deleteArtist(artist: Artist)
        suspend fun updateArtist(artist: Artist)
    }

    interface Remote{
        suspend fun getArtists(callback: ResultCallback<Result<List<Artist>>>)
        suspend fun getArtistFirebase(callback: ResultCallback<Result<List<Artist>>>)
    }



}