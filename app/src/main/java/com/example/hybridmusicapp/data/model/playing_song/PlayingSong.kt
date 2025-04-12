package com.example.hybridmusicapp.data.model.playing_song

import androidx.media3.common.MediaItem
import java.util.Objects

@Suppress("unused")
data class PlayingSong(
    private var _song: Song? = null,
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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PlayingSong) return false
        return playlist!!.id == other.playlist!!.id && song == other.song
    }

    override fun hashCode(): Int {
        return Objects.hash(song, playlist!!.id)
    }
}
