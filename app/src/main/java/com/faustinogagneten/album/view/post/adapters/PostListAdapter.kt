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


class PostListAdapter(private val mPostCallback: (PostsWithUser) -> Unit) : RecyclerView.Adapter<PostListAdapter.PostViewHolder>() {

    private var mPostsWithUserList: MutableList<PostsWithUser> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_post_layout, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {

        val post = mPostsWithUserList[position]

       holder.tvDescription!!.text = post.title
       holder.tvEmailUser!!.text = post.userEmail
       holder.itemView.setOnClickListener{ mPostCallback(post) }


    }

    override fun getItemCount(): Int {
        return mPostsWithUserList.size
    }

    fun setData(postList: MutableList<PostsWithUser>) {
        mPostsWithUserList = postList
        notifyDataSetChanged()
    }

    fun clearAdapter() {
        mPostsWithUserList.clear()
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        mPostsWithUserList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, mPostsWithUserList.size)
    }

    fun restoreItem(model: PostsWithUser, position: Int) {
        mPostsWithUserList.add(position, model)
        // notify item added by position
        notifyItemInserted(position)
    }

    /**
     * ViewHolder class
     */
    inner class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @JvmField
        @BindView(R.id.tvEmailUser)
        var tvEmailUser: TextView? = null
        @JvmField
        @BindView(R.id.tvDescription)
        var tvDescription: TextView? = null

        init {
            ButterKnife.bind(this, itemView)
        }
    }

}
