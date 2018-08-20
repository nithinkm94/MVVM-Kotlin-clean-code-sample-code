package com.faustinogagneten.album.repository.api

import com.faustinogagneten.album.db.entity.post.Post
import java.util.*

class PostResult {
    private val results = ArrayList<Post>()

    /**
     * @return The results
     */
    fun getResults(): List<Post> {
        return results
    }
}