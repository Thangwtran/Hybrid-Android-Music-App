package com.example.hybridmusicapp.data.model.song

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.Objects

@Entity(tableName = "songs")
open class Song(
    @SerializedName("id")
    @PrimaryKey
    @ColumnInfo(name = "song_id", defaultValue = "")
    var id: String = "",

    @SerializedName("title")
    @ColumnInfo(name = "title")
    var title: String = "",

    @SerializedName("album")
    @ColumnInfo(name = "album")
    var album: String = "",

    @SerializedName("artist")
    @ColumnInfo(name = "artist")
    var artist: String = "",

    @SerializedName("source")
    @ColumnInfo(name = "source")
    var source: String = "",

    @SerializedName("image")
    @ColumnInfo(name = "image")
    var image: String = "",

    @SerializedName("duration")
    @ColumnInfo(name = "duration")
    var duration: Int = 0,

    @ColumnInfo(name = "favorite")
    @Transient
    var isFavorite: Boolean = false,

    @SerializedName("counter")
    @ColumnInfo(name = "counter", defaultValue = "0")
    var counter: Int = 0,

    @ColumnInfo(name = "replay", defaultValue = "0")
    @Transient
    var replay: Int = 0
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Song) return false
        return id == other.id
    }

    override fun hashCode(): Int {
        return Objects.hash(id)
    }
}