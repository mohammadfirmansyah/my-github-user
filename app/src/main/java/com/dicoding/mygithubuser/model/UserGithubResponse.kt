package com.dicoding.mygithubuser.model

import com.google.gson.annotations.SerializedName

data class UserGithubResponse(

    @field:SerializedName("id")
    var id: Int? = null,

    @field:SerializedName("login")
    var login: String? = null,

    @field:SerializedName("html_url")
    var htmlUrl: String? = null,

    @field:SerializedName("public_repos")
    val publicRepos: Int? = null,

    @field:SerializedName("followers")
    val followers: Int? = null,

    @field:SerializedName("avatar_url")
    var avatarUrl: String? = null,

    @field:SerializedName("following")
    val following: Int? = null,

    @field:SerializedName("name")
    val name: String? = null
)
