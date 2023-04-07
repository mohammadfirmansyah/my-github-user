package com.dicoding.mygithubuser.ui.favorite

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.mygithubuser.R
import com.dicoding.mygithubuser.databinding.ActivityUserFavoriteBinding
import com.dicoding.mygithubuser.db.local.entity.UserEntity
import com.dicoding.mygithubuser.db.remote.response.UserGithubResponse
import com.dicoding.mygithubuser.factory.ViewModelFactory
import com.dicoding.mygithubuser.repository.UserFavoriteRepository
import com.dicoding.mygithubuser.ui.github.UserGithubActivity

class UserFavoriteActivity : AppCompatActivity() {

    private var _activityFavoriteBinding: ActivityUserFavoriteBinding? = null
    private lateinit var binding: ActivityUserFavoriteBinding
    private lateinit var userFavoriteViewModel: UserFavoriteViewModel
    private lateinit var adapter: UserFavoriteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = resources.getString(R.string.favorite)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        userFavoriteViewModel = obtainViewModel(this@UserFavoriteActivity)
        userFavoriteViewModel.getAllFavoriteUser().observe(this) { favoriteUserList ->
            if (favoriteUserList != null)
                adapter.setData(favoriteUserList)
        }
        adapter = UserFavoriteAdapter()

        binding.rvFavorite?.layoutManager = LinearLayoutManager(this)
        binding.rvFavorite?.setHasFixedSize(false)
        binding.rvFavorite?.adapter = adapter

        showRecyclerView()
    }

    private fun showRecyclerView() {
        binding.rvFavorite.layoutManager =
            if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                GridLayoutManager(this, 2)
            } else {
                LinearLayoutManager(this)
            }

        binding.rvFavorite.setHasFixedSize(true)
        binding.rvFavorite.adapter = adapter

        adapter.setOnItemClickCallback { data -> selectedUser(data) }
    }

    private fun selectedUser(user: UserEntity) {
        Toast.makeText(this, "Kamu memilih ${user.login}", Toast.LENGTH_SHORT).show()

        val i = Intent(this, UserGithubActivity::class.java)
        i.putExtra(UserGithubActivity.EXTRA_USER, user.login)
        startActivity(i)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        _activityFavoriteBinding = null
    }

    private fun obtainViewModel(activity: AppCompatActivity): UserFavoriteViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(UserFavoriteViewModel::class.java)
    }
}