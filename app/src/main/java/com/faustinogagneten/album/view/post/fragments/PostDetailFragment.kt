package com.faustinogagneten.album.view.post.fragments

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.faustinogagneten.album.R
import com.faustinogagneten.album.databinding.PostDetailFragmentBinding
import com.faustinogagneten.album.db.entity.Photo.PhotosWithAlbum
import com.faustinogagneten.album.db.entity.post.Post
import com.faustinogagneten.album.di.Injectable
import com.faustinogagneten.album.repository.util.Response
import com.faustinogagneten.album.repository.util.Status
import com.faustinogagneten.album.utils.DeviceUtils
import com.faustinogagneten.album.view.common.NavigationController
import com.faustinogagneten.album.view.post.adapters.AlbumListAdapter
import com.faustinogagneten.album.view.post.adapters.RelatedPostsListAdapter
import com.faustinogagneten.album.viewmodel.post.PostDetailViewModel
import java.util.*
import javax.inject.Inject

class PostDetailFragment : Fragment(), Injectable {

    @Inject
    @JvmField
    var viewModelFactory: ViewModelProvider.Factory? = null
    @Inject
    @JvmField
    var navigationController: NavigationController? = null
    private var mAlbumWithPhotosViewModel: PostDetailViewModel? = null
    private var mAlbumListAdapter: AlbumListAdapter? = null
    private var mRelatedPostsListAdapter: RelatedPostsListAdapter? = null
    var mPost: Post? = null
    private var mRelatedPostList: List<Post>? = null
    private var mPhotoWithAlbumList: List<PhotosWithAlbum>? = ArrayList()

    lateinit var binding: PostDetailFragmentBinding



    companion object {
        private const val BUNDLE_POST_ID = "POST_ID"

        fun newInstance(postId: Int): PostDetailFragment {
            val args = Bundle()
            args.putInt(BUNDLE_POST_ID, postId)

            val fragment = PostDetailFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.post_detail_fragment, container, false)

        loadPostDetail()
        return binding.root
    }

    private fun handleAppBarLayout() {
        binding.appBarLayout.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            var isShow = true
            var scrollRange = -1

            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.totalScrollRange

                }
                if (scrollRange + verticalOffset == 0) {
                    isShow = true
                    binding.toolbarLayout.title = mPost!!.title
                } else if (isShow) {
                    binding.toolbarLayout.title = " "
                    isShow = false
                }
            }
        })
    }

    private fun handlePostResponse(response: Response<Post>?) {
        if (response != null) {
            when (response.status) {
                Status.SUCCESS -> {
                    binding.includedPostDetail!!.tvError.visibility = View.GONE
                    if (response.data != null) {
                        mPost = response.data
                        updatePostUI()
                    } else {
                        binding.includedPostDetail!!.tvError.text = resources.getString(R.string.error_no_results)
                        binding.includedPostDetail!!.tvError.visibility = View.VISIBLE
                    }
                }
                else -> {
                    binding.includedPostDetail!!.tvError.text = resources.getString(R.string.error_no_results)
                }
            }
        }
    }

    private fun updatePostUI() {
        binding.includedPostHeader!!.tvPostTitle.text = mPost!!.title
        binding.includedPostDetail!!.tvPostBody.text = mPost!!.body
        mAlbumWithPhotosViewModel!!.setUserId(mPost!!.userId)
        handleAppBarLayout()
    }

    private fun handleRelatedPostsResponse(response: Response<List<Post>>?) {
        if (response != null) {
            when (response.status) {
                Status.SUCCESS -> {
                    binding.includedPostDetail!!.tvError.visibility = View.GONE
                    if (response.data != null) {
                        mRelatedPostList= response.data
                        updateRelatedPostUI()
                    } else {
                        binding.includedPostDetail!!.tvError.text = resources.getString(R.string.error_no_results)
                        binding.includedPostDetail!!.tvError.visibility = View.VISIBLE
                    }
                }
                else -> {
                    binding.includedPostDetail!!.tvError.text = resources.getString(R.string.error_no_results)
                }
            }
        }
    }

    private fun handleAlbumWithPhotosResponse(listResponse: Response<List<PhotosWithAlbum>>?) {
        if (listResponse != null) {
            when (listResponse.status) {
                Status.SUCCESS -> {
                    binding.includedPostDetail!!.tvError.visibility = View.GONE
                    if (listResponse.data != null && listResponse.data.isNotEmpty()) {
                        mPhotoWithAlbumList = listResponse.data
                        updateUI()
                    } else {
                        binding.includedPostDetail!!.tvError.text = resources.getString(R.string.error_no_results)
                        binding.includedPostDetail!!.tvError.visibility = View.VISIBLE
                    }
                }
                else -> {
                    binding.includedPostDetail!!.tvError.text = resources.getString(R.string.error_no_results)
                }
            }
        }
    }

    private fun updateUI() {
        mAlbumListAdapter!!.setData(mPhotoWithAlbumList as MutableList<PhotosWithAlbum>)
    }

    private fun updateRelatedPostUI() {
        val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        binding.includedPostDetail!!.rvRelatedPosts.layoutManager = layoutManager
        binding.includedPostDetail!!.rvRelatedPosts.isNestedScrollingEnabled = false

        mRelatedPostsListAdapter!!.setData(mRelatedPostList as MutableList<Post>)


    }

    private fun loadPostDetail() {
        loadHeaderBackground()
        initAdapters()
        val mLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding.includedPostDetail!!.rvAlbums.layoutManager = mLayoutManager
        binding.includedPostDetail!!.rvAlbums.isNestedScrollingEnabled = false

        binding.includedPostDetail!!.rvAlbums.setHasFixedSize(true)


        mAlbumWithPhotosViewModel= ViewModelProviders.of(this, viewModelFactory)
                .get(PostDetailViewModel::class.java)
        mAlbumWithPhotosViewModel!!.setPostId((arguments!!.getInt(BUNDLE_POST_ID)))


        mAlbumWithPhotosViewModel!!.post
                .observe(this, Observer<Response<Post>> { this.handlePostResponse(it) })

        mAlbumWithPhotosViewModel!!.relatedPosts
                .observe(this, Observer<Response<List<Post>>> { this.handleRelatedPostsResponse(it) })
        mAlbumWithPhotosViewModel!!.photosWithAlbum
                .observe(this, Observer<Response<List<PhotosWithAlbum>>> { this.handleAlbumWithPhotosResponse(it) })
    }

    private fun initAdapters() {
        mAlbumListAdapter = AlbumListAdapter()
        binding.includedPostDetail!!.rvAlbums.adapter = mAlbumListAdapter

        mRelatedPostsListAdapter = RelatedPostsListAdapter()
        binding.includedPostDetail!!.rvRelatedPosts.adapter = mRelatedPostsListAdapter
    }

    private fun loadHeaderBackground() {
        val r = Random()
        val value = r.nextInt(4 - 1) + 1
        val backgroundImageUrl = getString(R.string.name_background_header_text) + value
        Glide.with(context)
                .load(context!!.resources.getIdentifier(backgroundImageUrl, "drawable", context!!.packageName))
                .crossFade()
                .centerCrop()
                .thumbnail(0.5f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.ivHeaderBackground)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity!! as AppCompatActivity).setSupportActionBar(binding.toolbar)
        (activity!! as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity!! as AppCompatActivity).supportActionBar?.setHomeButtonEnabled(true)
        DeviceUtils.setTranslucentStatusBar(activity!!.window, android.R.color.transparent)

        binding.toolbar.setNavigationOnClickListener { activity!!.onBackPressed() }
    }


}
