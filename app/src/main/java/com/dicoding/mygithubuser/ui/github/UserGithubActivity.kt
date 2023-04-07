package com.dicoding.mygithubuser.ui.github

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.room.RoomMasterTable.TABLE_NAME
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.dicoding.mygithubuser.ui.main.MainViewModel
import com.dicoding.mygithubuser.R
import com.dicoding.mygithubuser.adapter.SectionsPagerAdapter
import com.dicoding.mygithubuser.databinding.ActivityUserGithubBinding
import com.dicoding.mygithubuser.db.local.entity.UserEntity
import com.dicoding.mygithubuser.db.remote.response.UserGithubResponse
import com.dicoding.mygithubuser.ui.factory.ViewModelFactory
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
    private lateinit var favoriteUser: UserEntity
    private val viewModel: MainViewModel by viewModels()
    private var isFavorite: Boolean = false

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

        viewModel.getUserGithubDetail.observe(this) { item ->
            setData(item)
        }

        username = intent.getStringExtra(EXTRA_USER).toString()
        showViewModel()
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
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private val userGithubViewModel by viewModels<UserGithubViewModel> {
        ViewModelFactory.getInstance(
            application
        )
    }

    private fun setFavoriteUser(value: Boolean) {
        binding.imageFavorite.setOnFavoriteChangeListener { _, favorite ->
            if (favorite) {
                binding.imageFavorite.isFavorite = true
                userGithubViewModel.insert(favoriteUser)
                Toast.makeText(this, "Menambahkan ke Daftar Favorit", Toast.LENGTH_SHORT).show()
            } else {
                userGithubViewModel.delete(favoriteUser)
                Toast.makeText(this, "Menghapus dari Daftar Favorit", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setData(item: UserGithubResponse) {
        binding.apply {
            tvName.text = item.name
            tvUsername.text = item.login
            tvRepositoryValue.text = item.publicRepos.toString()
            tvFollowersValue.text = item.followers.toString()
            tvFollowingValue.text = item.following.toString()
            Glide.with(this@UserGithubActivity)
                .load(item.avatarUrl)
                .into(imgAvatar)
        }
        favoriteUser = UserEntity(
            item.id,
            item.login,
            item.htmlUrl,
            item.publicRepos,
            item.followers,
            item.avatarUrl,
            item.following,
            item.name
        )
        userGithubViewModel.isFavorite(favoriteUser.id.toString()).observe(this@UserGithubActivity) { isFavorite ->
            binding.imageFavorite.isFavorite = isFavorite
            binding.imageFavorite.setOnFavoriteChangeListener { _, favorite ->
                if (favorite) {
                    userGithubViewModel.insert(favoriteUser)
                    Toast.makeText(this@UserGithubActivity, "Menambahkan ke Daftar Favorit", Toast.LENGTH_SHORT).show()
                } else {
                    userGithubViewModel.delete(favoriteUser)
                    Toast.makeText(this@UserGithubActivity, "Menghapus dari Daftar Favorit", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}