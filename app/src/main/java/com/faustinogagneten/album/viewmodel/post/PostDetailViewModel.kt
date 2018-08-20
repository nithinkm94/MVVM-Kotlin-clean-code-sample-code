package com.faustinogagneten.album.viewmodel.post

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import com.faustinogagneten.album.db.entity.Photo.PhotosWithAlbum
import com.faustinogagneten.album.db.entity.post.Post
import com.faustinogagneten.album.repository.AlbumRepository
import com.faustinogagneten.album.repository.util.Response
import com.faustinogagneten.album.utils.AbsentLiveData
import com.faustinogagneten.album.utils.Objects
import javax.inject.Inject


class PostDetailViewModel @Inject
internal constructor(albumRepository: AlbumRepository) : ViewModel() {

    val photosWithAlbum: LiveData<Response<List<PhotosWithAlbum>>>
    val post: LiveData<Response<Post>>
    val relatedPosts: LiveData<Response<List<Post>>>

    var userId = MutableLiveData<Int>()
    var postId = MutableLiveData<Int>()



    init {
        post = Transformations.switchMap(postId) { postId ->
            if (postId == null) {
                AbsentLiveData.create()
            } else {
                albumRepository.postById(postId)
            }
        }

        relatedPosts = Transformations.switchMap(userId) { userId ->
            if (userId == null) {
                AbsentLiveData.create()
            } else {
                albumRepository.relatedPostById(userId)
            }
        }

        photosWithAlbum = Transformations.switchMap(userId) { userId ->
            if (userId == null) {
                AbsentLiveData.create()
            } else {
                albumRepository.listAlbumWithPhotos(userId)
            }
        }
    }

    fun setUserId(userId: Int) {
        if (Objects.equals(userId, this.userId.value)) {
            return
        }

        this.userId.value = userId
    }

    fun setPostId(postId: Int) {
        if (Objects.equals(postId, this.postId.value)) {
            return
        }

        this.postId.value = postId
    }

}
