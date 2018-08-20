package com.faustinogagneten.album.db.entity.Photo

import android.arch.persistence.room.Relation


/***
 * Photos with Album Entity
 */
class PhotosWithAlbum {
    var id: Int = 0
    var title: String = ""
    @Relation(parentColumn = "id", entityColumn = "albumId")
    var photos: List<Photo>? = null
}