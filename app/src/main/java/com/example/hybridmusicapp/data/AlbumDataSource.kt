package com.example.hybridmusicapp.data

import com.example.hybridmusicapp.data.model.album.Album
import com.example.hybridmusicapp.data.model.album.AlbumSongCrossRef
import com.example.hybridmusicapp.data.model.album.AlbumWithSongs
import kotlinx.coroutines.flow.Flow

interface AlbumDataSource {
    interface Local{
        suspend fun getTop10Albums():List<Album>
        suspend fun getAlbums():List<Album>
        fun getAlbumWithSongs(albumId:Int): Flow<AlbumWithSongs>
        suspend fun saveAlbums(vararg album: Album):Boolean
        suspend fun saveAlbumCrossRef(vararg crossRef: AlbumSongCrossRef)
        suspend fun updateAlbum(album: Album)
        suspend fun deleteAlbum(album: Album)
    }

    interface Remote{
        // suspend fun getTop10Albums(callback)
        // suspend fun getAlbums(callback)
    }
}