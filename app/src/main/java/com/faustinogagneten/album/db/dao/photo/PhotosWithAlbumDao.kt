package com.faustinogagneten.album.db.dao.photo

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import com.faustinogagneten.album.db.entity.Photo.PhotosWithAlbum

/***
 * Album with Photos DAO
 */
@Dao
abstract class PhotosWithAlbumDao {

    @Query("SELECT * from Album WHERE userId = :id")
    abstract fun findPhtosWithAlbum(id: Int): LiveData<List<PhotosWithAlbum>>
}