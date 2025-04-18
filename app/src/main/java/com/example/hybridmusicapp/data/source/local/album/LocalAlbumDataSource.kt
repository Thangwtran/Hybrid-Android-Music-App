package com.example.hybridmusicapp.data.source.local.album

import com.example.hybridmusicapp.data.AlbumDataSource
import com.example.hybridmusicapp.data.model.album.Album
import com.example.hybridmusicapp.data.model.album.AlbumSongCrossRef
import com.example.hybridmusicapp.data.model.album.AlbumWithSongs
import kotlinx.coroutines.flow.Flow

class LocalAlbumDataSource(
    private val albumDao: AlbumDao
): AlbumDataSource.Local {
    override suspend fun getTop10Albums(): List<Album> {
        return albumDao.top10Albums
    }

    override suspend fun getAlbums(): List<Album> {
        return albumDao.albums
    }

    override fun getAlbumWithSongs(albumId: Int): Flow<AlbumWithSongs> {
        TODO("Not yet implemented")
    }

    override suspend fun saveAlbums(vararg album: Album): Boolean {
        val arr = albumDao.saveAlbums(*album)
        return arr.isNotEmpty()
    }

    override suspend fun saveAlbumCrossRef(vararg crossRef: AlbumSongCrossRef) {
        TODO("Not yet implemented")
    }

    override suspend fun updateAlbum(album: Album) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAlbum(album: Album) {
        TODO("Not yet implemented")
    }

}