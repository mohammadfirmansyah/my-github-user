package com.dicoding.mygithubuser.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dicoding.mygithubuser.ui.github.UserFollowersFragment
import com.dicoding.mygithubuser.ui.github.UserFollowingFragment

class SectionsPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = UserFollowersFragment()
            1 -> fragment = UserFollowingFragment()
        }

        return fragment as Fragment
    }
}