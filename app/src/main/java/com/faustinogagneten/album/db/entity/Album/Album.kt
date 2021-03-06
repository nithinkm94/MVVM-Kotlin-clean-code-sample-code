package com.faustinogagneten.album.db.entity.Album

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.TypeConverters
import com.faustinogagneten.album.db.AlbumTypeConverters
import com.faustinogagneten.album.db.entity.user.User
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/***
 * Album Entity
 */
@Entity
@TypeConverters(AlbumTypeConverters::class)
class Album(
        @PrimaryKey(autoGenerate = false)
        @field:SerializedName("id")
        @field:Expose
        var id: Int,
        @ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["userId"])
        @field:SerializedName(value = "userId")
        var userId: Int,
        @field:SerializedName(value = "title")
        var title: String
)
