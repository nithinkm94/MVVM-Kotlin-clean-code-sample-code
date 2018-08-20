package com.faustinogagneten.album.db.dao.post

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.faustinogagneten.album.db.entity.post.Post


/***
 * Post DAO
 */
@Dao
abstract class PostDao {

    @Query("SELECT * FROM Post")
    abstract fun findAll(): LiveData<List<Post>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertPosts(postsList: List<Post>)

    @Query("SELECT * FROM Post where userId = :userId LIMIT 5")
    abstract fun searchRelatedPosts(userId: Int): LiveData<List<Post>>

    @Query("SELECT * FROM Post where id = :postId")
    abstract fun searchPostById(postId: Int): LiveData<Post>
}