package com.dicoding.mygithubuser.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class SearchGithubResponse(

	@field:SerializedName("items")
	val items: List<ItemsItem>? = null
)

@Parcelize
data class ItemsItem(

	@field:SerializedName("login")
	val login: String? = null,

	@field:SerializedName("avatar_url")
	val avatarUrl: String? = null,

	@field:SerializedName("html_url")
	val htmlUrl: String? = null,

) : Parcelable
