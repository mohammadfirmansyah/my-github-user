package com.dicoding.mygithubuser.ui.github

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.mygithubuser.db.local.entity.UserEntity
import com.dicoding.mygithubuser.repository.UserFavoriteRepository

class UserGithubViewModel (application: Application) : ViewModel() {
    private val mFavoriteUserRepository: UserFavoriteRepository =
        UserFavoriteRepository(application)

    fun isFavorite(userId: String): LiveData<Boolean> =
        mFavoriteUserRepository.isFavorite(userId)

    fun insert(user: UserEntity) {
        mFavoriteUserRepository.insert(user)
    }

    fun delete(user: UserEntity) {
        mFavoriteUserRepository.delete(user)
    }
}