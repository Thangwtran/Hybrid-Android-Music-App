package com.example.hybridmusicapp.data.model.artist

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "artists")
data class Artist(
    @PrimaryKey
    @SerializedName("id")
    @ColumnInfo(name = "artist_id")
    var id: Int = 0,

    @ColumnInfo(name = "name")
    @SerializedName("name")
    val name: String = "",

    @SerializedName("avatar")
    @ColumnInfo(name = "avatar")
    val avatar: String = "",

    @SerializedName("interested")
    @ColumnInfo(name = "interested")
    var interested: Int = 0,

    @ColumnInfo(name = "care_about")
    @Transient // ignore this field when converting to JSON
    var isCareAbout: Boolean = false
)