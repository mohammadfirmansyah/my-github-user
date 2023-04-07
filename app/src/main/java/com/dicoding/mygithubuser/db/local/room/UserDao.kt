package com.dicoding.mygithubuser.db.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dicoding.mygithubuser.db.local.entity.UserEntity

@Dao
interface UserDao {
    @Query("SELECT * FROM FavoriteUser ORDER BY id ASC")
    fun getAllFavoriteUsers(): LiveData<List<UserEntity>>

    @Query("SELECT EXISTS(SELECT * FROM FavoriteUser WHERE id = :userId)")
    fun isFavorite(userId: String): LiveData<Boolean>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertUser(user: UserEntity)

    @Update
    fun updateUser(user: UserEntity)

    @Delete
    fun deleteUser(user: UserEntity)
}