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
import com.faustinogagneten.album.db.entity.Photo.Photo
import java.util.*


class PhotoListAdapter : RecyclerView.Adapter<PhotoListAdapter.PhotoViewHolder>() {

    private var mPhotoList: MutableList<Photo> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, arg1: Int): PhotoViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_photo_layout, parent, false)
        return PhotoViewHolder(view)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {

        val item = mPhotoList[position]

        holder.tvTitle!!.text = item.title

        Glide.with(holder.ivPhoto!!.context)
                .load(item.thumbnailUrl)
                .crossFade()
                .thumbnail(0.5f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.ivPhoto!!)

    }

    override fun getItemCount(): Int {
        return mPhotoList.size
    }

    fun setData(photoList: MutableList<Photo>) {
        mPhotoList = photoList
        notifyDataSetChanged()
    }

    fun clearAdapter() {
        mPhotoList.clear()
        notifyDataSetChanged()
    }

    /**
     * ViewHolder class
     */
    inner class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @JvmField
        @BindView(R.id.ivPhoto)
        var ivPhoto: ImageView? = null
        @JvmField
        @BindView(R.id.tvTitle)
        var tvTitle: TextView? = null

        init {
            ButterKnife.bind(this, itemView)

        }
    }

}