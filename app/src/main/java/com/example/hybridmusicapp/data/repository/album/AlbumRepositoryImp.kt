package com.example.hybridmusicapp.data.repository.album

import com.example.hybridmusicapp.ResultCallback
import com.example.hybridmusicapp.data.model.album.Album
import com.example.hybridmusicapp.data.model.album.AlbumList
import com.example.hybridmusicapp.data.model.album.AlbumSongCrossRef
import com.example.hybridmusicapp.data.model.album.AlbumWithSongs
import com.example.hybridmusicapp.data.source.AlbumDataSource
import com.example.hybridmusicapp.data.source.remote.RemoteAlbumDataSource
import com.example.hybridmusicapp.data.source.remote.Result
import kotlinx.coroutines.flow.Flow
import okhttp3.Callback
import javax.inject.Inject

class AlbumRepositoryImp @Inject constructor(
    private val localAlbumDataSource: AlbumDataSource.Local,
): AlbumRepository.Local, AlbumRepository.Remote {
    private val remoteAlbumDataSource: AlbumDataSource.Remote = RemoteAlbumDataSource()

    override suspend fun getTop10Albums(): List<Album> {
        return localAlbumDataSource.getTop10Albums()
    }

    override suspend fun getAlbums(): List<Album> {
        return localAlbumDataSource.getAlbums()
    }

    override fun getAlbumWithSongs(albumId: Int): Flow<AlbumWithSongs> {
        return localAlbumDataSource.getAlbumWithSongs(albumId)
    }

    override suspend fun saveAlbums(vararg albums: Album): Boolean {
        return localAlbumDataSource.saveAlbums(*albums)
    }

    override suspend fun saveAlbumCrossRef(vararg crossRefs: AlbumSongCrossRef) {
        return localAlbumDataSource.saveAlbumCrossRef(*crossRefs)
    }

    override suspend fun updateAlbum(album: Album) {
        return localAlbumDataSource.updateAlbum(album)
    }

    override suspend fun deleteAlbum(album: Album) {
        return localAlbumDataSource.deleteAlbum(album)
    }


    // REMOTE
    override suspend fun loadRemoteAlbums(): Result<AlbumList> {
        return remoteAlbumDataSource.loadRemoteAlbums()
    }

    override suspend fun addAlbumToFireStore(albums: List<Album>) {
        remoteAlbumDataSource.addAlbumToFireStore(albums)
    }

    override suspend fun getTop10AlbumsFireStore(callback: ResultCallback<Result<List<Album>>>) {
        remoteAlbumDataSource.getTop10Albums(callback)
    }

    override suspend fun getAlbumsFireStore(callback: ResultCallback<Result<List<Album>>>) {
        remoteAlbumDataSource.getAlbums(callback)
    }

}