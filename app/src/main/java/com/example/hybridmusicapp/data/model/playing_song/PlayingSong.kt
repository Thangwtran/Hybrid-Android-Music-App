package com.example.hybridmusicapp.data.model.playing_song

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import androidx.media3.common.MediaItem
import com.example.hybridmusicapp.data.model.playlist.Playlist
import com.example.hybridmusicapp.data.model.song.NCSong
import com.example.hybridmusicapp.data.model.song.Song
import java.util.Objects

data class PlayingSong(
    private var _song: Song? = null,
    private var _ncSong: NCSong? = null,
    var playlist: Playlist? = null,
    var mediaItem: MediaItem? = null,
    var nextSongIndex: Int = -1,
    var currentSongIndex: Int = -1,
    var currentPosition: Long = -1
) {
    var song: Song?
        get() = _song
        set(song) {
            if (song != null) {
                _song = song
                mediaItem = MediaItem.fromUri(song.source)
            }
        }

    var ncSong: NCSong? = _ncSong

    @SuppressLint("UseKtx")
    fun setNcsMediaItem(ncsSong: NCSong?, context: Context) {
        if (ncsSong != null) {
            _ncSong = ncsSong
            val rawUri =
                Uri.parse("android.resource://${context!!.packageName}/${ncsSong.audioRes}")
            mediaItem = MediaItem.fromUri(rawUri)
        }
    }


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PlayingSong) return false
        return playlist!!.id == other.playlist!!.id && song == other.song
    }

    override fun hashCode(): Int {
        return Objects.hash(song, playlist!!.id)
    }
}
