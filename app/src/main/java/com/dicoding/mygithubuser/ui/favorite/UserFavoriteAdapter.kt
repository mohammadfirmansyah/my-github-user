package com.dicoding.mygithubuser.ui.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.mygithubuser.databinding.ListUserBinding
import com.dicoding.mygithubuser.db.local.entity.UserEntity
import com.dicoding.mygithubuser.db.remote.response.UserGithubResponse

class UserFavoriteAdapter: RecyclerView.Adapter<UserFavoriteAdapter.ListViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback
    private val listFavoriteUser = ArrayList<UserEntity>()

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ListUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val fav = listFavoriteUser[position]
        holder.bind(fav)
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(
                listFavoriteUser[position]
            )
        }
    }

    class DiffUtilCallback(
        private val oldList: List<UserEntity>,
        private val newList: List<UserEntity>
    ) :
        DiffUtil.Callback() {

        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]

            return oldItem.javaClass == newItem.javaClass
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]

            return oldItem.hashCode() == newItem.hashCode()
        }

        @Override
        override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
            return super.getChangePayload(oldItemPosition, newItemPosition)
        }
    }

    class ListViewHolder(private val _binding: ListUserBinding) :
        RecyclerView.ViewHolder(_binding.root) {
        fun bind(fav: UserEntity) {
            _binding.tvAccount.text = fav.login
            _binding.tvUsername.text = fav.htmlUrl

            Glide.with(itemView.context)
                .load(fav.avatarUrl)
                .skipMemoryCache(true)
                .into(_binding.imgAvatar)
        }
    }

    override fun getItemCount(): Int {
        return listFavoriteUser.size
    }

    fun interface OnItemClickCallback {
        fun onItemClicked(selected: UserEntity)
    }

    fun setData(data: List<UserEntity>) {
        val diffCallback = DiffUtilCallback(listFavoriteUser, data)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        listFavoriteUser.clear()
        listFavoriteUser.addAll(data)
        diffResult.dispatchUpdatesTo(this)
    }
}