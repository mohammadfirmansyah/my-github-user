package com.dicoding.mygithubuser.repository

import android.app.Application
import com.dicoding.mygithubuser.db.local.room.UserDao
import com.dicoding.mygithubuser.db.local.room.UserFavoriteDatabase
import com.dicoding.mygithubuser.db.local.entity.UserEntity
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class UserFavoriteRepository(application: Application) {
    private val mUserDao: UserDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = UserFavoriteDatabase.getDatabase(application)
        mUserDao = db.userDao()
    }

    fun getAllUser() = mUserDao.getAllFavoriteUsers()

    fun isFavorite(userId: String) = mUserDao.isFavorite(userId)

    fun insert(user: UserEntity) {
        executorService.execute { mUserDao.insertUser(user) }
    }

    fun delete(user: UserEntity) {
        executorService.execute { mUserDao.deleteUser(user) }
    }

    fun update(user: UserEntity) {
        executorService.execute { mUserDao.updateUser(user) }
    }
}