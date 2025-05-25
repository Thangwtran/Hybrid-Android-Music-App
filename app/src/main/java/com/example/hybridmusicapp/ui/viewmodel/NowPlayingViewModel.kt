package com.example.hybridmusicapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.hybridmusicapp.data.model.playing_song.PlayingSong
import com.example.hybridmusicapp.data.model.playlist.Playlist
import com.example.hybridmusicapp.data.model.song.Song
import com.example.hybridmusicapp.data.repository.recent_song.RecentSongRepositoryImp
import com.example.hybridmusicapp.data.repository.search.SearchingRepositoryImp
import com.example.hybridmusicapp.data.repository.song.SongRepositoryImp
import com.example.hybridmusicapp.utils.MusicAppUtils.DefaultPlaylistName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NowPlayingViewModel private constructor(
    songRepository: SongRepositoryImp,
    searchRepository: SearchingRepositoryImp,
    recentSongRepository: RecentSongRepositoryImp
) : ViewModel() {

    /**
     * Repository
     */
    private var _songRepository: SongRepositoryImp = songRepository
    private val _searchRepository: SearchingRepositoryImp = searchRepository
    private val _recentSongRepository: RecentSongRepositoryImp = recentSongRepository

    private val _playingSong = PlayingSong()

    private val _playingSongLiveData = MutableLiveData<PlayingSong>()
    val currentPlayingSong: LiveData<PlayingSong> = _playingSongLiveData

    private var _playlistName: String = ""

    private val _currentPlaylist = MutableLiveData<Playlist?>()
    val currentPlaylist: LiveData<Playlist?> = _currentPlaylist

    // Map lưu trữ các playlist (mặc định và tùy chỉnh)
    private val _playlistMap: MutableMap<String, Playlist?> = HashMap()

    private val _indexToPlay = MutableLiveData<Int>()
    val indexToPlay: LiveData<Int> = _indexToPlay

    private val _miniPlayerVisibility = MutableLiveData<Boolean>()
    val isMiniPlayerVisible: LiveData<Boolean> = _miniPlayerVisibility

    val historySearchSongs: LiveData<List<Song>> =
        _searchRepository.historySearchedSongs.asLiveData()

    /**
     * init
     */
    init {
        // Khởi tạo Singleton và playlist mặc định
        synchronized(NowPlayingViewModel::class.java) {
            if (instance == null) {
                instance = this
            }
        }
        initPlaylists()
    }

    // Khởi tạo các playlist mặc định từ enum DefaultPlaylistName
    private fun initPlaylists() {
        for (playlistName in DefaultPlaylistName.entries.toTypedArray()) {
            val playlist = Playlist(_id = -1, name = playlistName.value)
            _playlistMap[playlistName.value] = playlist
        }
    }

    /**
     * Playlist Management
     */

    // Lấy playlist từ map theo tên, trả về null nếu không tìm thấy
    private fun getPlaylist(playlistName: String): Playlist? {
        return _playlistMap.getOrDefault(playlistName, null)
    }

    // Cập nhật playlist trong map
    private fun updatePlaylist(playlist: Playlist): Boolean {
        return _playlistMap.put(playlist.name, playlist) != null
    }

    // Thiết lập danh sách bài hát cho playlist
    fun setupPlaylist(songs: List<Song>?, playlistName: String) {
        if (songs != null) {
            val playlist = getPlaylist(playlistName)
            if (playlist != null) {
                playlist.updateSongList(songs)
                updatePlaylist(playlist)
            }
        }
    }

    // Cập nhật playlist hiện tại và đồng bộ với PlayingSong
    fun setCurrentPlaylist(playlist: Playlist) {
        _currentPlaylist.value = playlist
        if (_playingSong.playlist == null || _playingSong.playlist !== playlist) {
            _playingSong.playlist = playlist
        }
    }

    /**
     * Song Management
     */

    // Cập nhật bài hát đang phát dựa trên chỉ số trong playlist
    fun setPlayingSongIndex(index: Int) {
        if (index != -1 && _playingSong.playlist?.songs?.size ?: 0 > index) {
            val song = _playingSong.playlist!!.songs[index]
            _playingSong.apply {
                this.song = song
                currentPosition = 0
                currentSongIndex = index
            }
            updatePlayingSongLiveData()
        }
    }

    // Cập nhật LiveData của bài hát đang phát
    private fun updatePlayingSongLiveData() {
        _playingSongLiveData.value = _playingSong
    }

    // Cập nhật trạng thái yêu thích của bài hát trong DB
    fun updateFavouriteStatus(song: Song) {
        viewModelScope.launch(Dispatchers.IO) {
            song.isFavorite = true
            _songRepository.update(song)
        }
    }

    // Cập nhật số lần phát và phát lại của bài hát trong DB
    fun updateSongInDB(song: Song) {
        viewModelScope.launch(Dispatchers.IO) {
            song.counter += 1
            song.replay += 1
            _songRepository.update(song)
        }
    }

    // Cập nhật số lần phát của bài hát theo ID trong DB
    fun updateSongCounter(songId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _songRepository.updateSongCounter(songId)
        }
    }

    // Cập nhật bài hát đang phát
    fun setPlayingSongLiveData(playingSong: PlayingSong) {
        _playingSongLiveData.value = playingSong
    }

    // Đặt chỉ số bài hát cần phát
    fun setIndexToPlay(index: Int) {
        _indexToPlay.value = index
    }


    /**
     * Mini Player Management
     */
    // Cập nhật trạng thái hiển thị của mini player
    fun setMiniPlayerVisible(state: Boolean) {
        _miniPlayerVisibility.value = state
    }

    // Factory để tạo instance của NowPlayingViewModel với các repository
    class Factory(
        private val _songRepository: SongRepositoryImp,
        private val _searchingRepository: SearchingRepositoryImp,
        private val _recentSongRepository: RecentSongRepositoryImp
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(NowPlayingViewModel::class.java)) {
                return NowPlayingViewModel(
                    _songRepository,
                    _searchingRepository,
                    _recentSongRepository
                ) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    // Singleton để đảm bảo chỉ có một instance của ViewModel
    companion object {
        @Volatile
        var instance: NowPlayingViewModel? = null
            private set
    }
}