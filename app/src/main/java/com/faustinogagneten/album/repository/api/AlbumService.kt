package com.faustinogagneten.album.repository.api

import android.arch.lifecycle.LiveData
import com.faustinogagneten.album.db.entity.Album.Album
import com.faustinogagneten.album.db.entity.Photo.Photo
import com.faustinogagneten.album.db.entity.post.Post

import com.faustinogagneten.album.db.entity.user.User
import com.faustinogagneten.album.repository.util.ApiResponse

import retrofit2.http.GET


interface AlbumService {

    @get:GET("users")
    val users: LiveData<ApiResponse<List<User>>>

    @get:GET("posts")
    val posts: LiveData<ApiResponse<List<Post>>>

    @get:GET("albums")
    val albums: LiveData<ApiResponse<List<Album>>>

    @get:GET("photos")
    val photos: LiveData<ApiResponse<List<Photo>>>

}
