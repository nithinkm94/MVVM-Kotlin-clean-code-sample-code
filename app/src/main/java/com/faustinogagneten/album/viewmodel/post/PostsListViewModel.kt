package com.faustinogagneten.album.viewmodel.post

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import com.faustinogagneten.album.db.entity.Album.Album
import com.faustinogagneten.album.db.entity.Photo.Photo
import com.faustinogagneten.album.db.entity.post.Post
import com.faustinogagneten.album.db.entity.post.PostsWithUser
import com.faustinogagneten.album.db.entity.user.User
import com.faustinogagneten.album.repository.AlbumRepository
import com.faustinogagneten.album.repository.util.Response
import com.faustinogagneten.album.utils.AbsentLiveData
import java.util.*
import javax.inject.Inject


class PostsListViewModel @Inject
internal constructor(albumRepository: AlbumRepository) : ViewModel() {

    val postsWithUser: LiveData<Response<List<PostsWithUser>>>
    val users: LiveData<Response<List<User>>>
    val posts: LiveData<Response<List<Post>>>
    val albums: LiveData<Response<List<Album>>>
    val photos: LiveData<Response<List<Photo>>>

    val title = MutableLiveData<String>()
    val findPostsByTitle: LiveData<Response<List<PostsWithUser>>>


    init {
        users = albumRepository.listUsers
        posts = albumRepository.listPosts
        postsWithUser = albumRepository.listPostsWithUser
        albums = albumRepository.listAlbums
        photos = albumRepository.listPhotos

        findPostsByTitle = Transformations.switchMap(title) { query ->
            if (query == null || query.trim().isEmpty()) {
                AbsentLiveData.create()
            } else {
                albumRepository.findPostsByTitle(title.toString())
            }
        }


    }

    fun getSearchPostResults(): LiveData<Response<List<PostsWithUser>>> {
        return findPostsByTitle
    }

    fun setSearchPostTitle(originalInput: String) {
        val input = originalInput.toLowerCase(Locale.getDefault()).trim { it <= ' ' }
        if (Objects.equals(input, title.value)) {
            return
        }
        title.value = input
    }


}