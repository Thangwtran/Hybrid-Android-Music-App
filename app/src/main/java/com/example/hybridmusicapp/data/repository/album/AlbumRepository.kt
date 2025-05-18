package com.example.hybridmusicapp.data.repository.album

import com.example.hybridmusicapp.ResultCallback
import com.example.hybridmusicapp.data.model.album.Album
import com.example.hybridmusicapp.data.model.album.AlbumList
import com.example.hybridmusicapp.data.source.remote.Result

interface AlbumRepository {
    interface Local{
        suspend fun getTop10Albums(): List<Album>
        suspend fun getAlbums():List<Album>

        // TODO: album with song 
        suspend fun saveAlbums(vararg album: Album): Boolean

        // TODO: album cross ref 
        suspend fun updateAlbum(album: Album)
        suspend fun deleteAlbum(album: Album)

    }

    interface Remote{
        suspend fun loadRemoteAlbums(): Result<AlbumList>
        suspend fun addAlbumToFireStore(albums: List<Album>)
        suspend fun getTop10AlbumsFireStore(callback: ResultCallback<Result<List<Album>>>)
        suspend fun getAlbumsFireStore(callback: ResultCallback<Result<List<Album>>>)
    }
}