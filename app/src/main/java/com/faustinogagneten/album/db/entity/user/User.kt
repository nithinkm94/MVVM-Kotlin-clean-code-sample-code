package com.faustinogagneten.album.db.entity.user

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.TypeConverters
import com.faustinogagneten.album.db.AlbumTypeConverters
import com.faustinogagneten.album.db.entity.post.Post
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/***
 * User Entity
 */
@Entity
@TypeConverters(AlbumTypeConverters::class)
class User {
        @PrimaryKey(autoGenerate = false)
        @field:SerializedName("id")
        @Expose
        var id: Int = 0
        @Ignore
        var posts: List<Post>? = null
        @field:SerializedName(value = "name")
        var name: String = ""
        @field:SerializedName(value = "username")
        var userName: String = ""
        @field:SerializedName(value = "email")
        var email: String = ""
}

