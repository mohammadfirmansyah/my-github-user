package com.dicoding.mygithubuser.model

import com.google.gson.annotations.SerializedName

data class FollowGithubResponse(

	@field:SerializedName("login")
	val login: String? = null,

	@field:SerializedName("avatar_url")
	val avatarUrl: String? = null,

	@field:SerializedName("html_url")
	val htmlUrl: String? = null
)
