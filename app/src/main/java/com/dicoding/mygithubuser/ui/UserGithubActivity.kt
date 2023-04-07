package com.dicoding.mygithubuser.ui

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.dicoding.mygithubuser.viewmodel.MainViewModel
import com.dicoding.mygithubuser.R
import com.dicoding.mygithubuser.adapter.SectionsPagerAdapter
import com.dicoding.mygithubuser.databinding.ActivityUserGithubBinding
import com.dicoding.mygithubuser.db.DatabaseContract.FavoriteColumns.Companion.TABLE_NAME
import com.dicoding.mygithubuser.model.UserGithubResponse
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class UserGithubActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_USER = "extra_user"
        var username = String()

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.follower,
            R.string.following
        )
    }

    private lateinit var binding: ActivityUserGithubBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var helper: FavoriteHelper
    private var listUserFavorite = ArrayList<UserGithubResponse>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserGithubBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = resources.getString(R.string.detail_user)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        username = intent.getStringExtra(EXTRA_USER).toString()
        showViewModel()

        helper = FavoriteHelper.getInstance(applicationContext)
        helper.open()
    }

    private fun favoriteExist(user: String): Boolean {
        val choose: String = DatabaseContract.FavoriteColumns.LOGIN + " =?"
        val chooseArg = arrayOf(user)
        val limit = "1"

        helper = FavoriteHelper(this)
        helper.open()

        val dataBaseHelper = DatabaseHelper(this@UserGithubActivity)
        val database: SQLiteDatabase = dataBaseHelper.writableDatabase
        val cursor: Cursor =
            database.query(TABLE_NAME, null, choose, chooseArg, null, null, null, limit)
        val exists: Boolean = cursor.count > 0
        cursor.close()

        database.close()
        return exists
    }

    private fun showViewModel() {
        viewModel.getIsLoading.observe(this) { isLoading ->
            if (isLoading) {
                binding.progressBarDarker.visibility = View.VISIBLE
                binding.progressBar.visibility = View.VISIBLE
                binding.progressBarInfo1.visibility = View.VISIBLE
                binding.progressBarInfo2.visibility = View.VISIBLE
            } else {
                binding.progressBarDarker.visibility = View.GONE
                binding.progressBar.visibility = View.GONE
                binding.progressBarInfo1.visibility = View.GONE
                binding.progressBarInfo2.visibility = View.GONE
            }
        }
        viewModel.detailGithubUser(username)
        viewModel.getUserGithubDetail.observe(this) { detailGithubUser ->
            Glide.with(this)
                .load(detailGithubUser.avatarUrl)
                .skipMemoryCache(true)
                .into(binding.imgAvatar)

            binding.tvName.text = detailGithubUser.name
            binding.tvUsername.text = detailGithubUser.login
            binding.tvRepositoryValue.text = detailGithubUser.publicRepos.toString()
            binding.tvFollowersValue.text = detailGithubUser.followers.toString()
            binding.tvFollowingValue.text = detailGithubUser.following.toString()

            if (favoriteExist(username)) {
                binding.imageFavorite.isFavorite = true
                binding.imageFavorite.setOnFavoriteChangeListener { _, favorite ->
                    if (favorite) {
                        listUserFavorite = helper.queryAll()
                        helper.insert(detailGithubUser)
                        Toast.makeText(this, "Menambah ke Daftar Favorite", Toast.LENGTH_SHORT).show()
                    } else {
                        listUserFavorite = helper.queryAll()
                        helper.delete(username)
                        Toast.makeText(this, "Menghapus dari Daftar Favorite", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                binding.imageFavorite.setOnFavoriteChangeListener { _, favorite ->
                    if (favorite) {
                        listUserFavorite = helper.queryAll()
                        helper.insert(detailGithubUser)
                        Toast.makeText(this, "Menambah ke Daftar Favorite", Toast.LENGTH_SHORT).show()
                    } else {
                        listUserFavorite = helper.queryAll()
                        helper.delete(username)
                        Toast.makeText(this, "MMenghapus dari Daftar Favorite", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
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
        helper.close()
    }
}