package com.faustinogagneten.album.db.dao.photo

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.faustinogagneten.album.db.entity.Photo.Photo

/***
 * Photo DAO
 */
@Dao
abstract class PhotoDao {

    @Query("SELECT * FROM Photo")
    abstract fun findAll(): LiveData<List<Photo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertPhotos(photosList: List<Photo>)
}