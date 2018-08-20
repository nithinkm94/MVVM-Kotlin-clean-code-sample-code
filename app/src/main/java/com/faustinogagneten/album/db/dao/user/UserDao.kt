package com.faustinogagneten.album.db.dao.user

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.faustinogagneten.album.db.entity.user.User

/***
 * User DAO
 */
@Dao
abstract class UserDao {

    @Query("SELECT * FROM User")
    abstract fun findAll(): LiveData<List<User>>

    @Query("SELECT * FROM User WHERE User.id = :id")
    abstract fun findUserById(id: Int): LiveData<List<User>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun inserUsers(usersList: List<User>)
}