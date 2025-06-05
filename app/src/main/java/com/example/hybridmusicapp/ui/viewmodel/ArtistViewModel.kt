package com.example.hybridmusicapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.hybridmusicapp.ResultCallback
import com.example.hybridmusicapp.data.model.artist.Artist
import com.example.hybridmusicapp.data.model.artist.ArtistSongCrossRef
import com.example.hybridmusicapp.data.model.artist.ArtistWithSongs
import com.example.hybridmusicapp.data.model.playlist.Playlist
import com.example.hybridmusicapp.data.model.song.Song
import com.example.hybridmusicapp.data.repository.artist.ArtistRepositoryImp
import com.example.hybridmusicapp.data.source.remote.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale

class ArtistViewModel(
    private val artistRepository: ArtistRepositoryImp
) : ViewModel() {
    private var dispatcher: CoroutineDispatcher = Dispatchers.IO

    private val _artists = MutableLiveData<List<Artist>?>()

    private val _artist = MutableLiveData<Artist>()
    val artist: LiveData<Artist> = _artist

    private val _artistWithSongs = MutableLiveData<ArtistWithSongs>()
    val artistWithSongs: LiveData<ArtistWithSongs> = _artistWithSongs

    val remoteArtist: LiveData<List<Artist>?> = _artists

    val localArtists: LiveData<List<Artist>>
        get() = artistRepository.getLocalArtists().asLiveData()

    val playlist = Playlist()


    fun getArtistWithSongs(artistId: Int){
        viewModelScope.launch(dispatcher) {
            val result = artistRepository.getArtistWithSongs(artistId)
            setArtistWithSongs(result)
        }
    }

    fun setArtist(artist: Artist){
        _artist.postValue(artist)
    }

    fun setPlaylistSongs(artistWithSongs: ArtistWithSongs){
        val songs = artistWithSongs.songs
        val playlistName = artistWithSongs.artist?.name
        playlist.name = playlistName!!
        playlist.updateSongList(songs)

    }

    fun setArtistWithSongs(artistWithSongs: ArtistWithSongs){
        _artistWithSongs.postValue(artistWithSongs)
    }
    fun getArtists() {
        viewModelScope.launch(Dispatchers.IO) {
            artistRepository.getArtistFirebase(object : ResultCallback<Result<List<Artist>>> {
                override fun onResult(result: Result<List<Artist>>) {
                    if (result is Result.Success) {
                        _artists.postValue(result.data)
                    } else if (result is Result.Failure) {
                        _artists.postValue(null)
                    }
                }
            })
        }
    }

    fun getTop20Artists() {
        viewModelScope.launch(Dispatchers.IO) {
            artistRepository.getTop20ArtistFirebase(object : ResultCallback<Result<List<Artist>>> {
                override fun onResult(result: Result<List<Artist>>) {
                    if (result is Result.Success) {
                        _artists.postValue(result.data)
                    } else if (result is Result.Failure) {
                        _artists.postValue(null)
                    }
                }
            })
        }
    }

    fun loadRemoteArtist() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = artistRepository.loadRemoteArtists()
            if (result is Result.Success) {
                _artists.postValue(result.data.artists)
            } else if (result is Result.Failure) {
                // TODO: Handle exception
            }
        }
    }

    fun addArtistsToFireStore(artists: List<Artist>) {
        viewModelScope.launch(Dispatchers.IO) {
            artistRepository.addArtistToFireStore(artists)
        }
    }

    fun saveArtistToDB(artists: List<Artist>) {
        viewModelScope.launch(Dispatchers.IO) {
            val artists = extractArtistNotInDB()
            val artistToInsert = artists.toTypedArray<Artist>()
            artistRepository.insertArtists(*artistToInsert)
        }
    }

    private fun extractArtistNotInDB(): List<Artist> {
        val result : MutableList<Artist> = ArrayList()
        val localArtists = localArtists.value
        val remoteArtist = remoteArtist.value
        if(remoteArtist != null){
            if(localArtists == null){
                result.addAll(remoteArtist)
            }else{
                for(artist in remoteArtist){
                    if(!localArtists.contains(artist)){
                        result.add(artist)
                    }
                }
            }
        }else{
            Log.w("ArtistViewModel", "remoteArtist is null")
        }
        return result
    }

    fun saveArtistSongCrossRef(songs: List<Song>, artists: List<Artist>?){
        viewModelScope.launch(dispatcher) {
            val crossRefs: MutableList<ArtistSongCrossRef> = ArrayList()
            if(artists != null){
                for((artistId,name) in artists){
                    for(song in songs){
                        val key = ".*" + name.lowercase(Locale.getDefault()) + ".*"
                        if(song.artist.lowercase(Locale.getDefault()).matches(key.toRegex())){
                            crossRefs.add(ArtistSongCrossRef(artistId, song.id))
                        }
                    }
                }
            }else{
                Log.w("ArtistViewModel", "artists is null")
            }
            val crossRefToInsert = crossRefs.toTypedArray<ArtistSongCrossRef>()
            artistRepository.insertArtistSongCrossRef(*crossRefToInsert)
        }
    }

    fun insertArtist(vararg artists: Artist) {
        viewModelScope.launch(dispatcher) {
            artistRepository.insertArtists(*artists)
        }
    }

    fun updateArtist(artist: Artist) {
        viewModelScope.launch(dispatcher) {
            artistRepository.updateArtist(artist)
        }
    }

    fun deleteArtist(artist: Artist) {
        viewModelScope.launch(dispatcher) {
            artistRepository.deleteArtist(artist)
        }
    }

    class Factory(
        private val artistRepository: ArtistRepositoryImp
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ArtistViewModel::class.java)) {
                return ArtistViewModel(artistRepository) as T
            } else {
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }
}