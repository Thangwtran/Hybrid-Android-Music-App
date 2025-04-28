package com.example.hybridmusicapp.data.source.remote

import android.service.carrier.CarrierMessagingService
import com.example.hybridmusicapp.data.model.artist.Artist
import com.example.hybridmusicapp.data.source.ArtistDataSource
import kotlin.Result

class RemoteArtistDataSource:ArtistDataSource.Remote {
    override suspend fun getArtists(callback: CarrierMessagingService.ResultCallback<Result<List<Artist>>>) {
        TODO("Not yet implemented")
    }

    override suspend fun getArtistFirebase(callback: CarrierMessagingService.ResultCallback<Result<List<Artist>>>) {
        TODO("Not yet implemented")
    }
}