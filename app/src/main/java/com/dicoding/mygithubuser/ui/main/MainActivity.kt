package com.dicoding.mygithubuser.ui.main

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.mygithubuser.R
import com.dicoding.mygithubuser.databinding.ActivityMainBinding
import com.dicoding.mygithubuser.db.remote.response.ItemsItem
import com.dicoding.mygithubuser.factory.ViewModelFactory
import com.dicoding.mygithubuser.ui.theme.ThemePreferences
import com.dicoding.mygithubuser.ui.theme.ThemeViewModelFactory
import com.dicoding.mygithubuser.ui.theme.ThemeActivity
import com.dicoding.mygithubuser.ui.favorite.UserFavoriteActivity
import com.dicoding.mygithubuser.ui.github.UserGithubActivity
import com.dicoding.mygithubuser.ui.theme.ThemeViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModelTheme: ThemeViewModel
    private val viewModel: MainViewModel by viewModels()
    private val adapter = ListUserAdapter()
    private var initialSearch = "a"
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        darkModeCheck()
        showViewModel()
        showRecyclerView()

        binding.fabAdd.setOnClickListener {
            val i = Intent(this, UserFavoriteActivity::class.java)
            startActivity(i)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView
        val close = menu.findItem(R.id.search)

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                viewModel.searchGithubUser(query)
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })

        close.icon?.setVisible(false, false)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.setting) {
            startActivity(Intent(this, ThemeActivity::class.java))
        }

        return super.onOptionsItemSelected(item)
    }

    private fun showRecyclerView() {
        binding.rvUser.layoutManager =
            if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                GridLayoutManager(this, 2)
            } else {
                LinearLayoutManager(this)
            }

        binding.rvUser.setHasFixedSize(true)
        binding.rvUser.adapter = adapter

        adapter.setOnItemClickCallback{ data -> selectedUser(data) }
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
        viewModel.getSearchUserList.observe(this) { searchList ->
            if (searchList.size != 0) {
                binding.rvUser.visibility = View.VISIBLE
                adapter.setData(searchList)
            } else {
                binding.rvUser.visibility = View.GONE
                Toast.makeText(this, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.searchGithubUser(initialSearch)
    }

    private fun selectedUser(user: ItemsItem) {
        Toast.makeText(this, "Kamu memilih ${user.login}", Toast.LENGTH_SHORT).show()

        val i = Intent(this, UserGithubActivity::class.java)
        i.putExtra(UserGithubActivity.EXTRA_USER, user.login)
        startActivity(i)
    }

    private fun darkModeCheck() {
        val pref = ThemePreferences.getInstance(dataStore)
        viewModelTheme =
            ViewModelProvider(this, ThemeViewModelFactory(pref))[ThemeViewModel::class.java]

        viewModelTheme.getThemeSettings().observe(this@MainActivity) { isDarkModeActive ->
            if (isDarkModeActive) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}