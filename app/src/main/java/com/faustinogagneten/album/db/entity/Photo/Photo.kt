package com.faustinogagneten.album.db.entity.Photo

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.TypeConverters
import com.faustinogagneten.album.db.AlbumTypeConverters
import com.faustinogagneten.album.db.entity.Album.Album
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/***
 * Photo Entity
 */
@Entity
@TypeConverters(AlbumTypeConverters::class)
class Photo(
        @PrimaryKey(autoGenerate = false)
        @field:SerializedName("id")
        @field:Expose
        var id: Int,
        @ForeignKey(entity = Album::class, parentColumns = ["id"], childColumns = ["albumId"])

        @field:SerializedName(value = "albumId")
        var albumId: Int,
        @field:SerializedName(value = "title")
        var title: String,
        @field:SerializedName(value = "url")
        var url: String,
        @field:SerializedName(value = "thumbnailUrl")
        var thumbnailUrl: String
)