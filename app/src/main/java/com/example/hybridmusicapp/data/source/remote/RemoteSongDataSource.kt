package com.example.hybridmusicapp.data.source.remote

import android.util.Log
import com.example.hybridmusicapp.ResultCallback
import com.example.hybridmusicapp.data.model.song.Song
import com.example.hybridmusicapp.data.model.song.SongList
import com.example.hybridmusicapp.data.source.SongDataSource
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RemoteSongDataSource : SongDataSource.Remote {

    override suspend fun loadRemoteSongs(): Result<SongList> {
        return withContext(Dispatchers.IO) {
            val response = RetrofitHelper.instance.loadSongs()
            if (response.isSuccessful) {
                if (response.body() != null) {
                    Result.Success(response.body()!!)
                } else {
                    Result.Failure(Exception("No songs found"))
                }
            } else {
                Result.Failure(Exception(response.message()))
            }
        }
    }

    override suspend fun addSongToFireStore(songs: List<Song>) {
        val db = Firebase.firestore

        // batch -> use to write multiple documents in one transaction
        db.runBatch { batch -> // asynchronous -> not have suspend
            for (song in songs) {
                // document reference of each song in songs collection
                val docRef = db.collection("songs").document(song.id.toString())
                batch.set(docRef, song)
            }
        }.addOnSuccessListener {
            Log.d("FireStore", "Add Song To FireStore Successful")
        }.addOnFailureListener {
            Log.d("FireStore", "Add Song To FireStore Failed")
        }
    }


    override suspend fun getSongByArtist(artist: String): Result<List<Song>> {
        TODO("Not yet implemented")
    }

    override suspend fun getSongByTitle(title: String): Result<List<Song>> {
        TODO("Not yet implemented")
    }

    override suspend fun getSongById(
        songId: String,
        callback: ResultCallback<Result<Song>>
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun getTop10MostHeard(callback: ResultCallback<Result<List<Song>>>) {
        TODO("Not yet implemented")
    }

    override suspend fun getTop10Replay(callback: ResultCallback<Result<List<Song>>>) {
        TODO("Not yet implemented")
    }

    override suspend fun updateSongCounter(songId: String) {
        TODO("Not yet implemented")
    }


}