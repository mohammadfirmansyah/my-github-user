package com.dicoding.mygithubuser.ui.favorite

import android.app.Application
import androidx.lifecycle.*
import com.dicoding.mygithubuser.db.local.entity.UserEntity
import com.dicoding.mygithubuser.repository.UserFavoriteRepository

class UserFavoriteViewModel(application: Application) : ViewModel() {
    private val mFavoriteUserRepository: UserFavoriteRepository = UserFavoriteRepository(application)
    fun getAllFavoriteUser(): LiveData<List<UserEntity>> = mFavoriteUserRepository.getAllUser()
}