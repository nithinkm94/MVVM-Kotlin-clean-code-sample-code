package com.faustinogagneten.album.view.post.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.faustinogagneten.album.R
import com.faustinogagneten.album.db.entity.post.PostsWithUser
import java.util.*

class SearchPostAdapter(private val mPostClickCallback: (PostsWithUser) -> Unit) : RecyclerView.Adapter<SearchPostAdapter.PostViewHolder>() {

    private var mPostList: MutableList<PostsWithUser> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchPostAdapter.PostViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.search_view_suggestion_item, parent, false)
        return PostViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SearchPostAdapter.PostViewHolder, position: Int) {

        val post = mPostList[position]

        holder.tvPostTitle!!.text = post.title
        holder.tvUserEmail!!.text = post.userEmail
        holder.itemView.setOnClickListener{ mPostClickCallback(post) }
    }

    override fun getItemCount(): Int {
        return mPostList.size
    }

    inner class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @JvmField
        @BindView(R.id.postTitle)
        var tvPostTitle: TextView? = null

        @JvmField
        @BindView(R.id.userEmail)
        var tvUserEmail: TextView? = null

        init {
            ButterKnife.bind(this, itemView)
        }
    }

    fun clearAdapter() {
        mPostList.clear()
        notifyDataSetChanged()
    }

    fun setItems(postList: MutableList<PostsWithUser>) {
        mPostList = postList
        notifyDataSetChanged()
    }


}