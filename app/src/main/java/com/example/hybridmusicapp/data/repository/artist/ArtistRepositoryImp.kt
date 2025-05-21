package com.example.hybridmusicapp.data.repository.artist

import com.example.hybridmusicapp.ResultCallback
import com.example.hybridmusicapp.data.model.artist.Artist
import com.example.hybridmusicapp.data.model.artist.ArtistList
import com.example.hybridmusicapp.data.source.ArtistDataSource
import com.example.hybridmusicapp.data.source.remote.RemoteArtistDataSource
import kotlinx.coroutines.flow.Flow
import com.example.hybridmusicapp.data.source.remote.Result


class ArtistRepositoryImp(
    private val localDataSource: ArtistDataSource.Local,
) : ArtistRepository.Local, ArtistRepository.Remote {

    private val remoteDataSource: ArtistDataSource.Remote = RemoteArtistDataSource()

    // LOCAL
    override fun getLocalArtists(): Flow<List<Artist>> {
        return localDataSource.getArtists()
    }

    override suspend fun insertArtists(vararg artist: Artist) {
        localDataSource.insertArtist(*artist)
    }

    override suspend fun deleteArtist(artist: Artist) {
        localDataSource.deleteArtist(artist)
    }

    override suspend fun updateArtist(artist: Artist) {
        localDataSource.updateArtist(artist)
    }


    // REMOTE
    override suspend fun loadRemoteArtists(): Result<ArtistList> {
        return remoteDataSource.loadRemoteArtists()
    }

    override suspend fun addArtistToFireStore(artists: List<Artist>) {
        remoteDataSource.addArtistToFireStore(artists)
    }

    override suspend fun getTop20ArtistFirebase(callback: ResultCallback<Result<List<Artist>>>) {
        remoteDataSource.getTop20ArtistFirebase(callback)
    }

    override suspend fun getArtistFirebase(callback: ResultCallback<Result<List<Artist>>>) {
        remoteDataSource.getArtistFirebase(callback)
    }
}