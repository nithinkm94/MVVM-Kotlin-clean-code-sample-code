package com.faustinogagneten.album.di.module

import android.app.Application
import android.arch.persistence.room.Room
import com.faustinogagneten.album.db.AlbumDb
import com.faustinogagneten.album.db.dao.album.AlbumDao
import com.faustinogagneten.album.db.dao.photo.PhotoDao
import com.faustinogagneten.album.db.dao.photo.PhotosWithAlbumDao
import com.faustinogagneten.album.db.dao.post.PostDao
import com.faustinogagneten.album.db.dao.post.PostsWithUserDao
import com.faustinogagneten.album.db.dao.user.UserDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by fgagneten on 15/05/2017.
 * Room module. Basic configurations
 */
@Module
class RoomModule {

    @Singleton
    @Provides
    internal fun providesRoomDatabase(app: Application): AlbumDb {
        return Room.databaseBuilder(app, AlbumDb::class.java, "album_db").build()
    }

    @Singleton
    @Provides
    internal fun providePostDao(db: AlbumDb): PostDao {
        return db.postDao()
    }

    @Singleton
    @Provides
    internal fun provideUserDao(db: AlbumDb): UserDao {
        return db.userDao()
    }

    @Singleton
    @Provides
    internal fun provideAlbumDao(db: AlbumDb): AlbumDao {
        return db.albumDao()
    }

    @Singleton
    @Provides
    internal fun providePhotoDao(db: AlbumDb): PhotoDao {
        return db.photoDao()
    }

    @Singleton
    @Provides
    internal fun providePostsWithUserDao(db: AlbumDb): PostsWithUserDao {
        return db.postsWithUserDao()
    }

    @Singleton
    @Provides
    internal fun providePhotosWithAlbumDao(db: AlbumDb): PhotosWithAlbumDao {
        return db.photosWithAlbumDao()
    }
}
