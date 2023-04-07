package com.dicoding.mygithubuser.db

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "usersFavorite")
@Parcelize
data class UserEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val ID: Int,

    @ColumnInfo(name = "avatar_url")
    val AVATAR: String,

    @ColumnInfo(name = "html_url")
    val HTML: String,

    @ColumnInfo(name = "login")
    val login: String

) : Parcelable