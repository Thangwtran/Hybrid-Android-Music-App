package com.example.hybridmusicapp.data.repository.album

import com.example.hybridmusicapp.data.model.album.Album
import okhttp3.Callback

interface AlbumRepository {
    interface Local{
        suspend fun getTop10Albums(): List<Album>
        suspend fun getAlbums():List<Album>

        // TODO: album with song 
        suspend fun saveAlbums(albums: List<Album>): Boolean

        // TODO: album cross ref 
        suspend fun updateAlbum(album: Album): Boolean
        suspend fun deleteAlbum(album: Album): Boolean

    }

    interface Remote{
        suspend fun getTop10AlbumsRequest(callback: Callback)
        suspend fun getAlbumsRequest(callback: Callback)
    }
}