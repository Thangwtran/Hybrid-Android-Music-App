package com.example.hybridmusicapp.data.source.local.album

import com.example.hybridmusicapp.data.source.AlbumDataSource
import com.example.hybridmusicapp.data.model.album.Album
import com.example.hybridmusicapp.data.model.album.AlbumSongCrossRef
import com.example.hybridmusicapp.data.model.album.AlbumWithSongs
import kotlinx.coroutines.flow.Flow

class LocalAlbumDataSource(
    private val albumDao: AlbumDao
) : AlbumDataSource.Local {
    // TOP 10
    override suspend fun getTop10Albums(): List<Album> {
        return albumDao.top10Albums
    }

    // GET ALL ALBUMS
    override suspend fun getAlbums(): List<Album> {
        return albumDao.albums
    }

    override fun getAlbumWithSongs(albumId: Int): Flow<AlbumWithSongs> {
        TODO("Not yet implemented")
    }

    // SAVE ALBUMS
    override suspend fun saveAlbums(vararg album: Album): Boolean {
        val arr = albumDao.saveAlbums(*album)
        return arr.isNotEmpty()
    }

    override suspend fun saveAlbumCrossRef(vararg crossRef: AlbumSongCrossRef) {
        TODO("Not yet implemented")
    }

    override suspend fun updateAlbum(album: Album) {
        return albumDao.updateAlbum(album)
    }

    override suspend fun deleteAlbum(album: Album) {
        return albumDao.deleteAlbum(album)
    }

}