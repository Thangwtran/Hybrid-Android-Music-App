package com.example.hybridmusicapp.data.model.album

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.Objects

@Entity(tableName = "albums")
data class Album(
    @SerializedName("id") @PrimaryKey @ColumnInfo(name = "album_id")
    val id: Int = 0,

    @SerializedName("size") @ColumnInfo(name = "size")
    val size: Int = 0,

    @SerializedName("name") @ColumnInfo(name = "name")
    val name: String = "",

    @SerializedName("artwork") @ColumnInfo(name = "artwork")
    val artwork: String = "",
) {
    @SerializedName("songs") @Ignore // ignore this field when converting to JSON
    private val _songs: MutableList<String> = ArrayList()

    var songs: List<String>?
        get() = _songs
        set(songs) {
            _songs.clear()
            if (!songs.isNullOrEmpty()) {
                _songs.clear()
                _songs.addAll(songs)
            }
        }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Album) return false
        return id == other.id
    }

    override fun hashCode(): Int {
        return Objects.hash(id)
    }

    companion object {
        @Ignore
        private var autoId = 1000
    }
}