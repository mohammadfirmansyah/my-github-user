package com.dicoding.mygithubuser.repo

import android.app.Application
import android.provider.ContactsContract
import androidx.lifecycle.LiveData
import com.dicoding.mygithubuser.db.UserDao
import com.dicoding.mygithubuser.db.UserDatabase
import com.dicoding.mygithubuser.db.UserEntity
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class UserRepository(application: Application) {
    private val mUserDao: UserDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = UserDatabase.getDatabase(application)
        mUserDao = db.userDao()
    }

    fun getAllNotes(): LiveData<List<UserEntity>> = mUserDao.getUsers()

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