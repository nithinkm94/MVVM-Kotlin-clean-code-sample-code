package com.faustinogagneten.album.db.dao.album

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.faustinogagneten.album.db.entity.Album.Album

/***
 * Album DAO
 */
@Dao
abstract class AlbumDao {

    @Query("SELECT * FROM Album")
    abstract fun findAll(): LiveData<List<Album>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAlbums(albumsList: List<Album>)
}