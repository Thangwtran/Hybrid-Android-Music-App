package com.example.hybridmusicapp.data.source.remote

import com.example.hybridmusicapp.ResultCallback
import com.example.hybridmusicapp.data.model.album.Album
import com.example.hybridmusicapp.data.source.AlbumDataSource

class RemoteAlbumDataSource: AlbumDataSource.Remote {

    override suspend fun getTop10Albums(callback: ResultCallback<Result<List<Album>>>) {
        TODO("Not yet implemented")
    }

    override suspend fun getAlbums(callback: ResultCallback<Result<List<Album>>>) {
        TODO("Not yet implemented")
    }

}