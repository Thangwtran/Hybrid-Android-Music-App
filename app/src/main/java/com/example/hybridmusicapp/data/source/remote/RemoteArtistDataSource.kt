package com.example.hybridmusicapp.data.source.remote

import android.util.Log
import com.example.hybridmusicapp.ResultCallback
import com.example.hybridmusicapp.data.model.artist.Artist
import com.example.hybridmusicapp.data.model.artist.ArtistList
import com.example.hybridmusicapp.data.source.ArtistDataSource
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RemoteArtistDataSource : ArtistDataSource.Remote {

    override suspend fun loadRemoteArtists(result: Result<ArtistList>) {
        return withContext(Dispatchers.IO) {
            val response = RetrofitHelper.instance.loadArtist()
            if (response.isSuccessful) {
                if (response.body() != null) {
                    Result.Success(response.body()!!)
                } else {
                    Result.Failure(Exception("No artists found"))
                }
            } else {
                Result.Failure(Exception(response.message()))
            }
        }
    }

    override suspend fun addArtistToFireStore(artists: List<Artist>) {
        val db = Firebase.firestore
        db.runBatch { batch ->
            for (artist in artists) {
                val docRef = db.collection("artists").document(artist.id.toString())
                batch.set(docRef, artist)
            }
        }.addOnSuccessListener {
            Log.d("FireStore", "Add Artist To FireStore Successful")
        }.addOnFailureListener {
            Log.d("FireStore", "Add Artist To FireStore Failed")
        }
    }

    override suspend fun getArtists(callback: ResultCallback<Result<List<Artist>>>) {
        TODO("Not yet implemented")
    }

    override suspend fun getArtistFirebase(callback: ResultCallback<Result<List<Artist>>>) {
        TODO("Not yet implemented")
    }
}