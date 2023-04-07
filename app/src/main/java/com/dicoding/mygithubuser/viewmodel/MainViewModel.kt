package com.dicoding.mygithubuser.viewmodel

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.mygithubuser.api.ApiConfig
import com.dicoding.mygithubuser.model.FollowGithubResponse
import com.dicoding.mygithubuser.model.ItemsItem
import com.dicoding.mygithubuser.model.SearchGithubResponse
import com.dicoding.mygithubuser.model.UserGithubResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {

    private val searchList = MutableLiveData<ArrayList<ItemsItem>>()
    val getSearchUserList: LiveData<ArrayList<ItemsItem>> = searchList

    private val userGithub = MutableLiveData<UserGithubResponse>()
    val getUserGithubDetail: LiveData<UserGithubResponse> = userGithub

    private val followers = MutableLiveData<ArrayList<FollowGithubResponse>>()
    val getUserFollowers: LiveData<ArrayList<FollowGithubResponse>> = followers

    private val following = MutableLiveData<ArrayList<FollowGithubResponse>>()
    val getUserFollowing: LiveData<ArrayList<FollowGithubResponse>> = following

    private val isLoading = MutableLiveData<Boolean>()
    val getIsLoading: LiveData<Boolean> = isLoading

    private fun retry(func: () -> Unit) {
        Handler(Looper.getMainLooper()).postDelayed({
            func.invoke()
        }, 100)
    }

    fun searchGithubUser(username: String) {
        isLoading.value = true
        val client = ApiConfig.getApiService().searchUser(username)
        client.enqueue(object : Callback<SearchGithubResponse> {
            override fun onResponse(
                call: Call<SearchGithubResponse>,
                response: Response<SearchGithubResponse>
            ) {
                isLoading.value = false
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    searchList.value = responseBody.items?.let { ArrayList(it) }
                }
            }

            override fun onFailure(call: Call<SearchGithubResponse>, t: Throwable) {
                retry {
                    searchGithubUser(username)
                }
            }
        })
    }

    fun detailGithubUser(username: String) {
        isLoading.value = true
        val client = ApiConfig.getApiService().userGithub(username)
        client.enqueue(object : Callback<UserGithubResponse> {
            override fun onResponse(
                call: Call<UserGithubResponse>,
                response: Response<UserGithubResponse>
            ) {
                isLoading.value = false
                if (response.isSuccessful) {
                    userGithub.value = response.body()
                }
            }

            override fun onFailure(call: Call<UserGithubResponse>, t: Throwable) {
                retry {
                    detailGithubUser(username)
                }
            }
        })
    }

    fun userFollowers(username: String) {
        isLoading.value = true
        val client = ApiConfig.getApiService().userFollowers(username)
        client.enqueue(object : Callback<ArrayList<FollowGithubResponse>> {
            override fun onResponse(
                call: Call<ArrayList<FollowGithubResponse>>,
                response: Response<ArrayList<FollowGithubResponse>>
            ) {
                isLoading.value = false
                if (response.isSuccessful && response.body() != null) {
                    followers.value = response.body()
                }
            }

            override fun onFailure(call: Call<ArrayList<FollowGithubResponse>>, t: Throwable) {
                retry {
                    userFollowers(username)
                }
            }
        })
    }

    fun userFollowing(username: String) {
        isLoading.value = true
        val client = ApiConfig.getApiService().userFollowing(username)
        client.enqueue(object : Callback<ArrayList<FollowGithubResponse>> {
            override fun onResponse(
                call: Call<ArrayList<FollowGithubResponse>>,
                response: Response<ArrayList<FollowGithubResponse>>
            ) {
                isLoading.value = false
                if (response.isSuccessful && response.body() != null) {
                    following.value = response.body()
                }
            }

            override fun onFailure(call: Call<ArrayList<FollowGithubResponse>>, t: Throwable) {
                retry {
                    userFollowing(username)
                }
            }
        })
    }

}