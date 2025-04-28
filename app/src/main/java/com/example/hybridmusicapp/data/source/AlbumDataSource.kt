package com.example.hybridmusicapp.data.source

import com.example.hybridmusicapp.ResultCallback
import com.example.hybridmusicapp.data.model.album.Album
import com.example.hybridmusicapp.data.model.album.AlbumList
import com.example.hybridmusicapp.data.model.album.AlbumSongCrossRef
import com.example.hybridmusicapp.data.model.album.AlbumWithSongs
import com.example.hybridmusicapp.data.source.remote.Result
import kotlinx.coroutines.flow.Flow

interface AlbumDataSource {
    interface Local {
        suspend fun getTop10Albums(): List<Album>
        suspend fun getAlbums(): List<Album>
        fun getAlbumWithSongs(albumId: Int): Flow<AlbumWithSongs>
        suspend fun saveAlbums(vararg album: Album): Boolean
        suspend fun saveAlbumCrossRef(vararg crossRef: AlbumSongCrossRef)
        suspend fun updateAlbum(album: Album)
        suspend fun deleteAlbum(album: Album)
    }

    interface Remote {
        suspend fun loadRemoteAlbums(result: Result<AlbumList>)
        suspend fun addAlbumToFireStore(albums: List<Album>)
        suspend fun getTop10Albums(callback: ResultCallback<Result<List<Album>>>)
        suspend fun getAlbums(callback: ResultCallback<Result<List<Album>>>)
    }
}