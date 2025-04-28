package com.example.hybridmusicapp.data.source.remote

import android.util.Log
import com.example.hybridmusicapp.ResultCallback
import com.example.hybridmusicapp.data.model.album.Album
import com.example.hybridmusicapp.data.model.album.AlbumList
import com.example.hybridmusicapp.data.source.AlbumDataSource
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RemoteAlbumDataSource: AlbumDataSource.Remote {

    override suspend fun loadRemoteAlbums(result: Result<AlbumList>) {
        return withContext(Dispatchers.IO) {
            val response = RetrofitHelper.instance.loadPlaylist()
            if (response.isSuccessful) {
                if (response.body() != null) {
                    Result.Success(response.body()!!)
                } else {
                    Result.Failure(Exception("No albums found"))
                }
            }else{
                Result.Failure(Exception(response.message()))
            }
        }
    }

    override suspend fun addAlbumToFireStore(albums: List<Album>) {
        val db = Firebase.firestore
        db.runBatch { batch ->
            for (album in albums) {
                val docRef = db.collection("albums").document(album.id.toString())
                batch.set(docRef, album)
            }
        }.addOnSuccessListener {
            Log.d("FireStore", "Add Album To FireStore Successful")
        }.addOnFailureListener {
            Log.d("FireStore", "Add Album To FireStore Failed")
        }
    }

    override suspend fun getTop10Albums(callback: ResultCallback<Result<List<Album>>>) {
        TODO("Not yet implemented")
    }

    override suspend fun getAlbums(callback: ResultCallback<Result<List<Album>>>) {
        TODO("Not yet implemented")
    }

}