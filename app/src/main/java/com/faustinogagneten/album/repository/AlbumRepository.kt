package com.faustinogagneten.album.repository

import android.arch.lifecycle.LiveData
import android.content.SharedPreferences
import com.faustinogagneten.album.db.dao.album.AlbumDao
import com.faustinogagneten.album.db.dao.photo.PhotoDao
import com.faustinogagneten.album.db.dao.photo.PhotosWithAlbumDao
import com.faustinogagneten.album.db.dao.post.PostDao
import com.faustinogagneten.album.db.dao.post.PostsWithUserDao
import com.faustinogagneten.album.db.dao.user.UserDao
import com.faustinogagneten.album.db.entity.Album.Album
import com.faustinogagneten.album.db.entity.Photo.Photo
import com.faustinogagneten.album.db.entity.Photo.PhotosWithAlbum
import com.faustinogagneten.album.db.entity.post.Post
import com.faustinogagneten.album.db.entity.post.PostsWithUser
import com.faustinogagneten.album.db.entity.user.User
import com.faustinogagneten.album.repository.api.AlbumService
import com.faustinogagneten.album.repository.util.*
import com.faustinogagneten.album.utils.AppConstants.PREFS_COLD_START
import javax.inject.Inject
import javax.inject.Singleton

/**
 * This class helps manage communication from repository to ViewModels
 */

@Singleton
class AlbumRepository @Inject
internal constructor(
        private val mAppExecutors: AppExecutors,
        private val mAlbumService: AlbumService,
        private val mPostDao: PostDao,
        private val mUserDao: UserDao,
        private val mAlbumDao: AlbumDao,
        private val mPhotoDao: PhotoDao,
        private val mPostsWithUserDao: PostsWithUserDao,
        private val mPhotosWithAlbumDao: PhotosWithAlbumDao,
        preferences: SharedPreferences) {

    private val isColdStart = preferences.getBoolean(PREFS_COLD_START, true)

    val listPosts: LiveData<Response<List<Post>>>
        get() = object : NetworkBoundResource<List<Post>, List<Post>>(mAppExecutors) {

            override fun saveCallResult(item: List<Post>) {
                mPostDao.insertPosts(item)
            }

            override fun shouldFetch(data: List<Post>?): Boolean {
                return data == null || data.isEmpty() || isColdStart
            }

            override fun loadFromDb(): LiveData<List<Post>> {
                return mPostDao.findAll()
            }

            override fun createCall(): LiveData<ApiResponse<List<Post>>> {
                return mAlbumService.posts
            }
        }.asLiveData()

    fun findPostsByTitle (postTitle: String): LiveData<Response<List<PostsWithUser>>> {
        return object : OfflineBoundResource<List<PostsWithUser>>() {

            override fun loadFromDb(): LiveData<List<PostsWithUser>> {
                return mPostsWithUserDao.findPostsByTitle(postTitle)
            }
        }.asLiveData()
    }


    val listPostsWithUser: LiveData<Response<List<PostsWithUser>>>
        get() = object : OfflineBoundResource<List<PostsWithUser>>() {

            override fun loadFromDb(): LiveData<List<PostsWithUser>> {
                return mPostsWithUserDao.findPostWithUser()
            }
        }.asLiveData()

    fun postById (postId: Int): LiveData<Response<Post>> {
        return object : OfflineBoundResource<Post>() {

            override fun loadFromDb(): LiveData<Post> {
                return mPostDao.searchPostById(postId)
            }
        }.asLiveData()
    }

    fun relatedPostById (userId: Int): LiveData<Response<List<Post>>> {
        return object : OfflineBoundResource<List<Post>>() {

            override fun loadFromDb(): LiveData<List<Post>> {
                return mPostDao.searchRelatedPosts(userId)
            }
        }.asLiveData()
    }

    fun listAlbumWithPhotos (userId: Int):LiveData<Response<List<PhotosWithAlbum>>> {
        return object : OfflineBoundResource<List<PhotosWithAlbum>>() {

            override fun loadFromDb(): LiveData<List<PhotosWithAlbum>> {

                return mPhotosWithAlbumDao.findPhtosWithAlbum(userId)
            }
        }.asLiveData()
    }

    val listUsers: LiveData<Response<List<User>>>
        get() = object : NetworkBoundResource<List<User>, List<User>>(mAppExecutors) {

            override fun saveCallResult(item: List<User>) {
                mUserDao.inserUsers(item)
            }

            override fun shouldFetch(data: List<User>?): Boolean {
                return data == null || data.isEmpty() || isColdStart
            }

            override fun loadFromDb(): LiveData<List<User>> {
                return mUserDao.findAll()
            }

            override fun createCall(): LiveData<ApiResponse<List<User>>> {
                return mAlbumService.users
            }
        }.asLiveData()

    val listAlbums: LiveData<Response<List<Album>>>
        get() = object : NetworkBoundResource<List<Album>, List<Album>>(mAppExecutors) {

            override fun saveCallResult(item: List<Album>) {
                mAlbumDao.insertAlbums(item)
            }

            override fun shouldFetch(data: List<Album>?): Boolean {
                return data == null || data.isEmpty() || isColdStart
            }

            override fun loadFromDb(): LiveData<List<Album>> {
                return mAlbumDao.findAll()
            }

            override fun createCall(): LiveData<ApiResponse<List<Album>>> {
                return mAlbumService.albums
            }
        }.asLiveData()

    val listPhotos: LiveData<Response<List<Photo>>>
        get() = object : NetworkBoundResource<List<Photo>, List<Photo>>(mAppExecutors) {

            override fun saveCallResult(item: List<Photo>) {
                mPhotoDao.insertPhotos(item)
            }

            override fun shouldFetch(data: List<Photo>?): Boolean {
                return data == null || data.isEmpty() || isColdStart
            }

            override fun loadFromDb(): LiveData<List<Photo>> {
                return mPhotoDao.findAll()
            }

            override fun createCall(): LiveData<ApiResponse<List<Photo>>> {
                return mAlbumService.photos
            }
        }.asLiveData()
}
