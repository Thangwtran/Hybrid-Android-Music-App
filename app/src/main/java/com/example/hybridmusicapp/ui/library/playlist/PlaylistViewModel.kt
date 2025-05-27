package com.example.hybridmusicapp.ui.library.playlist

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
import com.example.hybridmusicapp.data.model.song.Song
import com.example.hybridmusicapp.data.repository.playlist.PlaylistRepositoryImp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class PlaylistViewModel(
    private val repository: PlaylistRepositoryImp
) : ViewModel() {

    private val _playlists =
        MutableLiveData<List<PlaylistWithSongs>>() // lưu danh sách các playlist
    private val _playlistWithSongs =
        MutableLiveData<PlaylistWithSongs>() // lưu danh sách các bài hát trong playlist

    val playlists: LiveData<List<PlaylistWithSongs>>
        get() = _playlists
    val playlistWithSong: LiveData<PlaylistWithSongs>
        get() = _playlistWithSongs

    fun loadPlaylist(): Flow<List<Playlist>> {
        return repository.getAllPlaylists()
    }

    fun loadPlaylistWithSongs(callback: ResultCallback<List<PlaylistWithSongs>>?) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllPlaylistWithSongs().collect { playlists ->
                setPlaylists(playlists)
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

    fun setPlaylists(playlists: List<PlaylistWithSongs>) {
        _playlists.postValue(playlists)
    }

    private fun setPlaylistWithSongs(playlistWithSongs: PlaylistWithSongs) {
        _playlistWithSongs.postValue(playlistWithSongs)
    }

    class Factory(
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