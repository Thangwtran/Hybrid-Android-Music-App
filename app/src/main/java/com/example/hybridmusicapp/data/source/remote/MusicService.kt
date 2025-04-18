package com.example.hybridmusicapp.data.source.remote

import com.example.hybridmusicapp.data.model.album.AlbumList
import com.example.hybridmusicapp.data.model.artist.ArtistList
import com.example.hybridmusicapp.data.model.song.SongList
import retrofit2.Response
import retrofit2.http.GET

interface MusicService {
    @GET("/resources/braniumapis/songs.json")
    suspend fun loadSongs():Response<SongList>

    @GET("/resources/braniumapis/playlist.json")
    suspend fun loadPlaylist():Response<AlbumList>

    @GET("/resources/braniumapis/artists.json")
    suspend fun loadArtist():Response<ArtistList>
}