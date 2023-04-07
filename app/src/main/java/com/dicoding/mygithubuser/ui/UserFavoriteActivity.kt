package com.dicoding.mygithubuser.ui

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.mygithubuser.R
import com.dicoding.mygithubuser.adapter.UserFavoriteAdapter
import com.dicoding.mygithubuser.databinding.ActivityUserFavoriteBinding
import com.dicoding.mygithubuser.db.FavoriteHelper
import com.dicoding.mygithubuser.model.UserGithubResponse

class UserFavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserFavoriteBinding
    private lateinit var helper: FavoriteHelper
    private var listFavorite: ArrayList<UserGithubResponse> = ArrayList()
    private val adapter = UserFavoriteAdapter()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = resources.getString(R.string.favorite)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        helper = FavoriteHelper.getInstance(applicationContext)
        helper.open()

        showRecyclerView()
        getDataFavorite()
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

    private fun selectedUser(user: UserGithubResponse) {
        Toast.makeText(this, "You choose ${user.login}", Toast.LENGTH_SHORT).show()

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

    override fun onResume() {
        super.onResume()
        getDataFavorite()
    }

    private fun getDataFavorite() {
        listFavorite = helper.queryAll()
        if (listFavorite.size > 0) {
            binding.rvFavorite.visibility = View.VISIBLE
            binding.noFavoriteData.visibility = View.GONE
            adapter.setData(listFavorite)
        } else {
            binding.rvFavorite.visibility = View.GONE
            binding.noFavoriteData.visibility = View.VISIBLE
        }
    }
}