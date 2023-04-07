package com.dicoding.mygithubuser.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.dicoding.mygithubuser.db.UserEntity
import com.dicoding.mygithubuser.repo.UserRepository

class FavoriteUserViewModel(application: Application) : ViewModel() {
    private val mUserRepository: UserRepository = UserRepository(application)
    fun getAllNotes(): LiveData<List<UserEntity>> = mUserRepository.getAllNotes()
}