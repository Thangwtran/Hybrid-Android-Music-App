package com.example.hybridmusicapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.hybridmusicapp.ResultCallback
import com.example.hybridmusicapp.data.model.playlist.Playlist
import com.example.hybridmusicapp.data.model.playlist.PlaylistSongCrossRef
import com.example.hybridmusicapp.data.model.playlist.PlaylistWithSongs
import com.example.hybridmusicapp.data.model.song.NCSong
import com.example.hybridmusicapp.data.model.song.Song
import com.example.hybridmusicapp.data.repository.playlist.PlaylistRepositoryImp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlaylistViewModel @Inject constructor(
    private val repository: PlaylistRepositoryImp
) : ViewModel() {

    // playlist
    private val _listPlaylist = MutableLiveData<List<PlaylistWithSongs>>()
    val playlists: LiveData<List<PlaylistWithSongs>>
        get() = _listPlaylist

    // playlist with songs
    private val _playlistWithSongs = MutableLiveData<PlaylistWithSongs>()
    val playlistWithSong: LiveData<PlaylistWithSongs>
        get() = _playlistWithSongs

    // playlist with ncs songs
    private val _ncsPlaylist = MutableLiveData<List<NCSong>>()
    val ncsPlaylist: LiveData<List<NCSong>>
        get() = _ncsPlaylist

    fun loadPlaylist(): Flow<List<Playlist>> {
        return repository.getAllPlaylists()
    }

    fun loadPlaylistWithSongs() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllPlaylistWithSongs().collect { playlists ->
                setListPlaylist(playlists)
            }
        }
    }

    fun loadPlaylistWithSongsByPlaylistId(playlistId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val playlist = repository.getPlaylistWithSongByPlaylistId(playlistId)
            playlist.collect {
                setPlaylistWithSongs(it)
            }
        }
    }

    fun createPlaylistCrossRef(playlist: Playlist, song: Song?, callback: ResultCallback<Boolean>) {
        if (song != null) {
            val playlistCrossRef = PlaylistSongCrossRef()
            playlistCrossRef.playlistId = playlist.id
            playlistCrossRef.songId = song.id
            viewModelScope.launch(Dispatchers.IO) {
                repository.insertPlaylistSongCrossRef(playlistCrossRef)
                callback.onResult(true)
            }
        }
        callback.onResult(false)
    }

    fun createNewPlaylist(playlistName: String?) {
        if (playlistName != null) {
            val playlist = Playlist(name = playlistName)
            viewModelScope.launch(Dispatchers.IO) {
                repository.insertPlaylist(playlist)
            }
        } else {
            Log.d("PlaylistViewModel", "Playlist name is null")
        }
    }

    fun updatePlaylist(playlist: Playlist) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updatePlaylist(playlist)
        }
    }

    fun deletePlaylist(playlist: Playlist) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deletePlaylist(playlist)
        }
    }

    fun setListPlaylist(playlists: List<PlaylistWithSongs>) {
        _listPlaylist.postValue(playlists)
    }

    fun setNcsPlaylist(ncsSongs: List<NCSong>) {
        _ncsPlaylist.postValue(ncsSongs)
    }

    private fun setPlaylistWithSongs(playlistWithSongs: PlaylistWithSongs) {
        _playlistWithSongs.postValue(playlistWithSongs)
    }

    class Factory @Inject constructor(
        private val playlistRepositoryImp: PlaylistRepositoryImp
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(PlaylistViewModel::class.java)) {
                return PlaylistViewModel(playlistRepositoryImp) as T
            } else {
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }
}