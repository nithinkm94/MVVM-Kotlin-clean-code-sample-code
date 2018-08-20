package com.faustinogagneten.album.view.post.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.faustinogagneten.album.R
import com.faustinogagneten.album.db.entity.post.Post


class RelatedPostsListAdapter : RecyclerView.Adapter<RelatedPostsListAdapter.RelatedVideosViewHolder>() {

    private var mRelatedVideosList: MutableList<Post> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RelatedVideosViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_photo_layout, parent, false)
        return RelatedVideosViewHolder(view)
    }

    override fun onBindViewHolder(holder: RelatedVideosViewHolder, position: Int) {

        val relatedPost = mRelatedVideosList[position]

        holder.tvRelatedVideoTitle!!.text = relatedPost.title
        Glide.with(holder.ivPhoto!!.context)
                .load(R.drawable.background_related_posts)
                .crossFade()
                .thumbnail(0.5f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.ivPhoto!!)

    }

    override fun getItemCount(): Int {
        return mRelatedVideosList.size
    }

    fun setData(postList: MutableList<Post>) {
        mRelatedVideosList = postList
        notifyDataSetChanged()
    }

    fun clearAdapter() {
        mRelatedVideosList.clear()
        notifyDataSetChanged()
    }

    /**
     * ViewHolder class
     */
    inner class RelatedVideosViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @JvmField
        @BindView(R.id.ivPhoto)
        internal var ivPhoto: ImageView? = null
        @JvmField
        @BindView(R.id.tvTitle)
        internal var tvRelatedVideoTitle: TextView? = null

        init {
            ButterKnife.bind(this, itemView)
        }
    }

}