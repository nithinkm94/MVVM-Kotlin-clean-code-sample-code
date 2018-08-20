package com.faustinogagneten.album.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.faustinogagneten.album.db.dao.album.AlbumDao
import com.faustinogagneten.album.db.dao.photo.PhotoDao
import com.faustinogagneten.album.db.dao.photo.PhotosWithAlbumDao
import com.faustinogagneten.album.db.dao.post.PostDao
import com.faustinogagneten.album.db.dao.post.PostsWithUserDao
import com.faustinogagneten.album.db.dao.user.UserDao
import com.faustinogagneten.album.db.entity.Album.Album
import com.faustinogagneten.album.db.entity.Photo.Photo
import com.faustinogagneten.album.db.entity.post.Post
import com.faustinogagneten.album.db.entity.user.User

/**
 * Main database which contains DAOs.
 */
@Database(entities = arrayOf(Post::class, User::class, Album::class, Photo::class), version = 1, exportSchema = false)
abstract class AlbumDb : RoomDatabase() {

    abstract fun postDao(): PostDao

    abstract fun userDao(): UserDao

    abstract fun albumDao(): AlbumDao

    abstract fun photoDao(): PhotoDao

    abstract fun postsWithUserDao(): PostsWithUserDao

    abstract fun photosWithAlbumDao(): PhotosWithAlbumDao
}
