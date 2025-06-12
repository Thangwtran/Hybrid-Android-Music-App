package com.example.hybridmusicapp.data.source.remote

import android.util.Log
import android.widget.Toast
import com.example.hybridmusicapp.ResultCallback
import com.example.hybridmusicapp.data.model.artist.Artist
import com.example.hybridmusicapp.data.model.artist.ArtistList
import com.example.hybridmusicapp.data.source.ArtistDataSource
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RemoteArtistDataSource @Inject constructor() : ArtistDataSource.Remote {
    private val firestore = FirebaseFirestore.getInstance();

    override suspend fun loadRemoteArtists(): Result<ArtistList> {
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

    override suspend fun getTop20ArtistFirebase(callback: ResultCallback<Result<List<Artist>>>) {
        withContext(Dispatchers.IO) {
            firestore.collection("artists")
                .orderBy("interested", Query.Direction.DESCENDING)
                .limit(20)
                .get()
                .addOnCompleteListener { task: Task<QuerySnapshot> ->
                    if (task.isSuccessful) {
                        val artists = task.result.toObjects(Artist::class.java)
                        callback.onResult(Result.Success(artists))
                    } else {
                        Log.e("RemoteArtistDataSource", "Error getting documents")
                        callback.onResult(Result.Failure(task.exception))
                    }
                }
                .addOnFailureListener { exception ->
                    callback.onResult(Result.Failure(exception))
                }
        }
    }

    override suspend fun getArtistFirebase(callback: ResultCallback<Result<List<Artist>>>) {
        withContext(Dispatchers.IO) {
            firestore.collection("artists")
                .get()
                .addOnCompleteListener { task: Task<QuerySnapshot> ->
                    if (task.isSuccessful) {
                        val artists = task.result.toObjects(Artist::class.java)
                        callback.onResult(Result.Success(artists))
                    } else {
                        Log.e("RemoteArtistDataSource", "Error getting documents")
                    }
                }
                .addOnFailureListener { exception ->
                    callback.onResult(Result.Failure(exception))
                }
        }
    }
}