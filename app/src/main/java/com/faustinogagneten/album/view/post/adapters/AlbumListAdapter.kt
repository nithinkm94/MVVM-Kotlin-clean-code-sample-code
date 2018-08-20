package com.faustinogagneten.album.view.post.adapters


import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.faustinogagneten.album.R
import com.faustinogagneten.album.db.entity.Photo.Photo
import com.faustinogagneten.album.db.entity.Photo.PhotosWithAlbum


class AlbumListAdapter : RecyclerView.Adapter<AlbumListAdapter.AlbumViewHolder>() {

    private var mAlbumsWithPhotosList: MutableList<PhotosWithAlbum> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_album_layout, parent, false)
        return AlbumViewHolder(view)
    }

    override fun getItemCount(): Int {

        return mAlbumsWithPhotosList.size
    }

    fun setData(albumsWithPhotosList: MutableList<PhotosWithAlbum>) {
        mAlbumsWithPhotosList = albumsWithPhotosList
        notifyDataSetChanged()
    }

    fun clearAdapter() {
        mAlbumsWithPhotosList.clear()
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {

        val photoListRow = mAlbumsWithPhotosList[position]

        holder.tvSubtitle!! .text = photoListRow.title

        val layoutManager = LinearLayoutManager(holder.itemView.context, LinearLayoutManager.HORIZONTAL, false)
        holder.rvPhoto!!.layoutManager = layoutManager
        holder.rvPhoto!!.isNestedScrollingEnabled = false
        holder.rvPhoto!!.setHasFixedSize(true)
        val photoListAdapter = PhotoListAdapter()
        photoListAdapter.setData(photoListRow.photos as MutableList<Photo>)
        holder.rvPhoto!!.adapter = photoListAdapter
    }

    inner class AlbumViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @JvmField
        @BindView(R.id.rvPhoto)
        var rvPhoto: RecyclerView? = null
        @JvmField
        @BindView(R.id.tvSubtitle)
        var tvSubtitle: TextView? = null

        init {
            ButterKnife.bind(this, itemView)
        }
    }




    companion object {

        val LOGTAG = "carousels"
    }

}
