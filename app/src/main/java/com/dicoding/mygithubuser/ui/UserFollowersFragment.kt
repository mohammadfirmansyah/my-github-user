package com.dicoding.mygithubuser.ui

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.mygithubuser.viewmodel.MainViewModel
import com.dicoding.mygithubuser.adapter.FollowFragmentAdapter
import com.dicoding.mygithubuser.databinding.FragmentUserFollowersBinding
import com.dicoding.mygithubuser.model.FollowGithubResponse

class UserFollowersFragment : Fragment() {

    private lateinit var binding: FragmentUserFollowersBinding
    private val _binding get() = binding
    private val adapter = FollowFragmentAdapter()
    private val viewModel: MainViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showViewModel()
        showRecyclerView()
        viewModel.getIsLoading.observe(viewLifecycleOwner, this::showLoading)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserFollowersBinding.inflate(inflater, container, false)
        return _binding.root
    }

    private fun showViewModel() {
        viewModel.getIsLoading.observe(viewLifecycleOwner) { isLoading ->
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
        viewModel.userFollowers(UserGithubActivity.username)
        viewModel.getUserFollowers.observe(viewLifecycleOwner) { followers ->
            if (followers.size != 0) {
                adapter.setData(followers)
            } else {
                Toast.makeText(context, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showRecyclerView() {
        val orientation = resources.configuration.orientation

        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            binding.rvFollowers.layoutManager = LinearLayoutManager(requireActivity())
        } else {
            binding.rvFollowers.layoutManager = GridLayoutManager(requireActivity(), 2)
        }

        binding.rvFollowers.setHasFixedSize(true)
        binding.rvFollowers.adapter = adapter

        adapter.setOnItemClickCallback { data -> selectedUser(data) }
    }

    private fun selectedUser(user: FollowGithubResponse) {
        Toast.makeText(context, "Kamu memilih ${user.login}", Toast.LENGTH_SHORT).show()

        val i = Intent(activity, UserGithubActivity::class.java)
        i.putExtra(UserGithubActivity.EXTRA_USER, user.login)
        startActivity(i)
    }

    private fun showLoading(isLoading: Boolean) =
        binding.progressBar.visibility == if (isLoading) View.VISIBLE else View.GONE

    override fun onResume() {
        super.onResume()
        viewModel.userFollowers(UserGithubActivity.username)
    }
}