package com.dicoding.mygithubuser.db.remote.api

import com.dicoding.mygithubuser.db.remote.response.FollowGithubResponse
import com.dicoding.mygithubuser.db.remote.response.SearchGithubResponse
import com.dicoding.mygithubuser.db.remote.response.UserGithubResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("search/users")
    fun searchUser(
        @Query("q") username: String
    ): Call<SearchGithubResponse>

    @GET("users/{username}")
    fun userGithub(
        @Path("username") username: String
    ): Call<UserGithubResponse>

    @GET("users/{username}/followers")
    fun userFollowers(
        @Path("username") username: String
    ): Call<ArrayList<FollowGithubResponse>>

    @GET("users/{username}/following")
    fun userFollowing(
        @Path("username") username: String
    ): Call<ArrayList<FollowGithubResponse>>
}