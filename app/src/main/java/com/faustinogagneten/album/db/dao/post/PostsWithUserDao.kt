package com.faustinogagneten.album.db.dao.post

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import com.faustinogagneten.album.db.entity.post.PostsWithUser

/***
 * Post with User DAO
 */
@Dao
abstract class PostsWithUserDao {

    @Query("SELECT p.id AS 'idPost', p.title AS 'title', p.body AS 'body', u.email AS 'userEmail' FROM Post p INNER JOIN User u ON (p.userId = u.id)")
    abstract fun findPostWithUser(): LiveData<List<PostsWithUser>>

    @Query("SELECT p.id AS 'idPost', p.title AS 'title', p.body AS 'body', u.email AS 'userEmail' FROM Post p INNER JOIN User u ON (p.userId = u.id) where p.title LIKE :postTitle")
    abstract fun findPostsByTitle(postTitle: String): LiveData<List<PostsWithUser>>
}