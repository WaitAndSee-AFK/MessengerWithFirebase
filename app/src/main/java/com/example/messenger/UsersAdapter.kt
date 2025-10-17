package com.example.messenger

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class UsersAdapter : RecyclerView.Adapter<UsersAdapter.UserViewHolder>() {
    var onUserClickListener: OnUserClickListener? = null
    private var users: List<User> = mutableListOf()

    fun updateUsers(newUsers: List<User>) {
        users = newUsers
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = users.size

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(
            R.layout.user_item,
            parent,
            false
        )
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: UserViewHolder,
        position: Int
    ) {
        val user = users.get(position)
        val userInfo = String.format("%s %s, %s", user.name, user.surname, user.age)
        holder.textViewUserInfo.text = userInfo
        val backgroundResID = if (user.isOnline) R.drawable.circle_green else R.drawable.circle_red
        val background: Drawable = ContextCompat.getDrawable(
            holder.itemView.context,
            backgroundResID
        )
        holder.viewOnlineStatus.background = background
        holder.itemView.setOnClickListener {
            onUserClickListener?.onUserClick(user)
        }
    }

    fun interface OnUserClickListener {
        fun onUserClick(user: User)
    }

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewUserInfo: TextView = itemView.findViewById(R.id.textViewUserInfo)
        val viewOnlineStatus: View = itemView.findViewById(R.id.viewOnlineStatus)
    }
}