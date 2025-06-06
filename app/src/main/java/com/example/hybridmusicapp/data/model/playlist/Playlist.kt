package com.example.hybridmusicapp.data.model.playlist

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.RawResourceDataSource
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.example.hybridmusicapp.data.model.song.NCSong
import com.example.hybridmusicapp.data.model.song.Song
import java.util.Date
import java.util.Objects
import kotlin.coroutines.coroutineContext


@Suppress("unused")
@Entity(tableName = "playlists")
data class Playlist(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "playlist_id")
    private var _id: Int = 1001,

    @ColumnInfo(name = "name", defaultValue = "")
    var name: String = "",

    @ColumnInfo(name = "image_artwork")
    var imageArtwork: String? = null,

    @ColumnInfo(name = "created_at")
    var createdAt: Date? = null
) {
    @Ignore
    var songs: List<Song> = listOf()
//        private set

    @Ignore
    var ncsSongs: List<NCSong> = listOf()
//        private set

    @Ignore
    private val _mediaItems: MutableList<MediaItem> = ArrayList()

    var id: Int
        get() = _id
        set(id) {
            _id = if (id != -1) {
                id
            } else {
                autoId++
            }
        }

    val mediaItems: List<MediaItem>
        get() = _mediaItems

    fun updateSongList(songs: List<Song>) {
        this.songs = songs
        updateMediaItems()
    }

    fun updateNcsSongList(ncsSongs: List<NCSong>, context: Context) {
        this.ncsSongs = ncsSongs
        updateNcsMediaItems(context)
    }

    private fun updateMediaItems() {
        _mediaItems.clear()
        for (song in songs) {
            _mediaItems.add(MediaItem.fromUri(song.source))
        }
    }

    @SuppressLint("UseKtx")
    private fun updateNcsMediaItems(context: Context) {
        _mediaItems.clear()
        for (ncs in ncsSongs) {
            val rawUri = Uri.parse("android.resource://${context!!.packageName}/${ncs.audioRes}")
            val mediaItem = MediaItem.fromUri(rawUri)
            _mediaItems.add(mediaItem)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Playlist) return false
        return id == other.id
    }

    override fun hashCode(): Int {
        return Objects.hash(id)
    }

    companion object {
        @Ignore
        private var autoId = 1001
    }
}