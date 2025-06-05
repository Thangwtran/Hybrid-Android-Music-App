package com.example.hybridmusicapp.data.source


import com.example.hybridmusicapp.ResultCallback
import com.example.hybridmusicapp.data.model.artist.Artist
import com.example.hybridmusicapp.data.model.artist.ArtistList
import com.example.hybridmusicapp.data.model.artist.ArtistSongCrossRef
import com.example.hybridmusicapp.data.model.artist.ArtistWithSongs
import kotlinx.coroutines.flow.Flow
import com.example.hybridmusicapp.data.source.remote.Result

interface ArtistDataSource {
    interface Local {
        fun getArtists(): Flow<List<Artist>>

        suspend fun getArtistWithSongs(artistId: Int): ArtistWithSongs

        suspend fun insertArtist(vararg artist: Artist)

        suspend fun insertArtistSongCrossRef(vararg values: ArtistSongCrossRef)

        suspend fun deleteArtist(artist: Artist)

        suspend fun updateArtist(artist: Artist)
    }

    interface Remote {
        suspend fun loadRemoteArtists(): Result<ArtistList>
        suspend fun addArtistToFireStore(artists: List<Artist>)
        suspend fun getTop20ArtistFirebase(callback: ResultCallback<Result<List<Artist>>>)
        suspend fun getArtistFirebase(callback: ResultCallback<Result<List<Artist>>>)
    }


}