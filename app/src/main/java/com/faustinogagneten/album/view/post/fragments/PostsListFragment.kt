package com.faustinogagneten.album.view.post.fragments

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.SharedPreferences
import android.databinding.DataBindingUtil
import android.graphics.*
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import com.faustinogagneten.album.R
import com.faustinogagneten.album.databinding.PostListFragmentBinding
import com.faustinogagneten.album.db.entity.Album.Album
import com.faustinogagneten.album.db.entity.Photo.Photo
import com.faustinogagneten.album.db.entity.post.Post
import com.faustinogagneten.album.db.entity.post.PostsWithUser
import com.faustinogagneten.album.db.entity.user.User
import com.faustinogagneten.album.di.Injectable
import com.faustinogagneten.album.repository.util.Response
import com.faustinogagneten.album.repository.util.Status
import com.faustinogagneten.album.utils.AppConstants
import com.faustinogagneten.album.utils.DeviceUtils
import com.faustinogagneten.album.view.common.NavigationController
import com.faustinogagneten.album.view.post.adapters.PostListAdapter
import com.faustinogagneten.album.view.post.adapters.SearchPostAdapter
import com.faustinogagneten.album.viewmodel.post.PostsListViewModel
import java.util.*
import javax.inject.Inject


class PostsListFragment : Fragment(), Injectable {

    @Inject
    @JvmField
    var viewModelFactory: ViewModelProvider.Factory? = null
    @Inject
    @JvmField
    var navigationController: NavigationController? = null

    var searchPostListIsShowing = false
    var mPostsViewModel: PostsListViewModel? = null
    var mPostListAdapter: PostListAdapter? = null
    var mPostList: List<PostsWithUser>? = ArrayList()
    var searchPostsWithUserAdapter: SearchPostAdapter? = null
    lateinit var binding: PostListFragmentBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.post_list_fragment, container, false)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater!!.inflate(R.menu.menu_main, menu)
        binding.toolbar!!.setNavigationOnClickListener {
            if (searchPostListIsShowing) {
                binding.searchResults.visibility = View.GONE
            } else {
                activity!!.onBackPressed()
            }
        }

        val mSearchItem = menu!!.findItem(R.id.search_item).actionView as SearchView
        mSearchItem.setIconifiedByDefault(true)



        mSearchItem.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(title: String): Boolean {
                binding.searchResults.visibility = View.VISIBLE
                searchPostListIsShowing = true
                findPosts(title)
                hideKeyboard()
                return false
            }
            override fun onQueryTextChange(title: String): Boolean {
                binding.searchResults.visibility = View.VISIBLE
                Log.i("d","Press querytextchange")

                findPosts(title)
                return true
            }
        })

        mSearchItem.setOnCloseListener(object : SearchView.OnCloseListener {
            override fun onClose(): Boolean {
                binding.searchResults.visibility = View.GONE
                searchPostsWithUserAdapter!!.clearAdapter()
                return false
            }
        })


    }

    fun findPosts(title: String) {
        if (!title.isEmpty() || !title.equals("")) {
            val filteredModelList: List<PostsWithUser> = filter(mPostList!!, title)
            if (filteredModelList.isEmpty()) {
                mPostsViewModel!!.setSearchPostTitle(title)
            } else {
                Log.d("add", "paso por aca")
                searchPostsWithUserAdapter!!.setItems(filteredModelList as MutableList<PostsWithUser>)
                searchPostsWithUserAdapter!!.notifyDataSetChanged()
            }
        }

    }

    fun filter(models: List<PostsWithUser>, query: String) : List<PostsWithUser>  {
        val finalQuery = query.toLowerCase()
        val filteredModelList: MutableList<PostsWithUser>  = ArrayList()
        for (model: PostsWithUser in models) {
            val text: String = model.title.toLowerCase()
            if (text.contains(finalQuery)) {
                filteredModelList.add(model)
            }
        }
        return filteredModelList
    }

    fun hideKeyboard() {
        val view = activity!!.currentFocus
        if(android.os.Build.VERSION.SDK_INT >= 26) {
            val imm: InputMethodManager = context!!.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            view?.post {
                imm.hideSoftInputFromWindow(activity!!.currentFocus.windowToken, 0)
                imm.hideSoftInputFromInputMethod(activity!!.currentFocus.windowToken, 0)
            }
        } else {
            if (view != null) {
                val imm = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
                imm.hideSoftInputFromInputMethod(view.windowToken, 0)
            }
        }
    }



    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
        setHasOptionsMenu(true)
        DeviceUtils.setTranslucentStatusBar(activity!!.window, R.color.colorPrimaryDark)

        // The content do not change the layout size (improve performance)
        binding.recyclerView.setHasFixedSize(true)

        val mLayoutManager = LinearLayoutManager(activity)
        binding.recyclerView.layoutManager = mLayoutManager

        mPostListAdapter = PostListAdapter {
            mPost: PostsWithUser -> navigationController!!.navigateToDetailFragment(mPost.idPost) }
        binding.recyclerView.adapter = mPostListAdapter

        mPostsViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(PostsListViewModel::class.java)

        getUsers()
    }

    private fun getUsers() {

        mPostsViewModel!!.users
                .observe(this, Observer<Response<List<User>>> { this.handleUsersResponse(it) })
    }

    private fun handleUsersResponse(listResponse: Response<List<User>>?) {
        if (listResponse != null) {
            when (listResponse.status) {
                Status.ERROR -> {
                    binding.progressbar.visibility = View.GONE
                    binding.tvError.visibility = View.VISIBLE
                    binding.tvError.text = resources.getString(R.string.error_loading)
                }
                Status.LOADING -> {
                    binding.progressbar.visibility = View.VISIBLE
                    binding.tvError.visibility = View.GONE
                }
                Status.SUCCESS -> {
                   getPosts()
                }
            }
        }
    }

    private fun getPosts() {
        mPostsViewModel!!.posts
                .observe(this, Observer<Response<List<Post>>> { this.handlePostsResponse(it) })
    }

    private fun handlePostsResponse(listResponse: Response<List<Post>>?) {
        if (listResponse != null) {
            when (listResponse.status) {
                Status.ERROR -> {
                    binding.progressbar.visibility = View.GONE
                    binding.tvError.visibility = View.VISIBLE
                    binding.tvError.text = resources.getString(R.string.error_loading)
                }
                Status.LOADING -> {
                    binding.progressbar.visibility = View.VISIBLE
                    binding.tvError.visibility = View.GONE
                }
                Status.SUCCESS -> {
                    getAlbums()
                }
            }
        }
    }

    private fun getAlbums() {
        mPostsViewModel!!.albums
                .observe(this, Observer<Response<List<Album>>> { this.handleAlbumsResponse(it) })
    }

    private fun handleAlbumsResponse(listResponse: Response<List<Album>>?) {
        if (listResponse != null) {
            when (listResponse.status) {
                Status.ERROR -> {
                    binding.progressbar.visibility = View.GONE
                    binding.tvError.visibility = View.VISIBLE
                    binding.tvError.text = resources.getString(R.string.error_loading)
                }
                Status.LOADING -> {
                    binding.progressbar.visibility = View.VISIBLE
                    binding.tvError.visibility = View.GONE
                }
                Status.SUCCESS -> {
                    getPhotos()
                }
            }
        }
    }

    private fun getPhotos() {
        mPostsViewModel!!.photos
                .observe(this, Observer<Response<List<Photo>>> { this.handlePhotosResponse(it) })
    }

    private fun handlePhotosResponse(listResponse: Response<List<Photo>>?) {
        if (listResponse != null) {
            when (listResponse.status) {
                Status.ERROR -> {
                    binding.progressbar.visibility = View.GONE
                    binding.tvError.visibility = View.VISIBLE
                    binding.tvError.text = resources.getString(R.string.error_loading)
                }
                Status.LOADING -> {
                    binding.progressbar.visibility = View.VISIBLE
                    binding.tvError.visibility = View.GONE
                }
                Status.SUCCESS -> {
                    getPostsWithUser()
                }
            }
        }
    }

    private fun getPostsWithUser() {
        mPostsViewModel!!.postsWithUser
                .observe(this, Observer<Response<List<PostsWithUser>>> { this.handlePostsWithUserResponse(it) })

        mPostsViewModel!!.getSearchPostResults()
                .observe(this, Observer<Response<List<PostsWithUser>>> { this.handleSearchPostResponse(it) })

        val linearLayoutManager = LinearLayoutManager(
                activity, LinearLayoutManager.VERTICAL, false
        )
        binding.searchResults.layoutManager = linearLayoutManager

        searchPostsWithUserAdapter = SearchPostAdapter { mPost: PostsWithUser -> navigationController!!.navigateToDetailFragment(mPost.idPost) }
        binding.searchResults.adapter = searchPostsWithUserAdapter
    }

    private fun handlePostsWithUserResponse(listResponse: Response<List<PostsWithUser>>?) {
        if (listResponse != null) {
            when (listResponse.status) {
                Status.ERROR -> {
                    binding.progressbar.visibility = View.GONE
                    binding.tvError.visibility = View.VISIBLE
                    binding.tvError.text = resources.getString(R.string.error_loading)
                }
                Status.LOADING -> {
                    binding.progressbar.visibility = View.VISIBLE
                    binding.tvError.visibility = View.GONE
                }
                Status.SUCCESS -> {
                    binding.progressbar.visibility = View.GONE
                    binding.tvError.visibility = View.GONE
                    if (listResponse.data != null && listResponse.data.isNotEmpty()) {
                        mPostList = listResponse.data
                        updateUI()
                    } else {
                        binding.tvError.text = resources.getString(R.string.error_no_results)
                        binding.tvError.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun handleSearchPostResponse(listResponse: Response<List<PostsWithUser>>?) {
        if (listResponse != null) {
            when (listResponse.status) {
                Status.ERROR -> {
                    binding.progressbar.visibility = View.GONE
                    binding.tvError.visibility = View.VISIBLE
                    binding.tvError.text = resources.getString(R.string.error_loading)
                }
                Status.LOADING -> {
                    binding.progressbar.visibility = View.VISIBLE
                    binding.tvError.visibility = View.GONE
                }
                Status.SUCCESS -> {
                    binding.progressbar.visibility = View.GONE
                    binding.tvError.visibility = View.GONE
                    if (listResponse.data != null && listResponse.data.isNotEmpty()) {
                        searchPostsWithUserAdapter!!.setItems(listResponse.data as MutableList<PostsWithUser>)
                        searchPostsWithUserAdapter!!.notifyDataSetChanged()
                    }
                }
            }
        }
    }

    private fun updateUI() {
        mPostListAdapter!!.setData(mPostList as MutableList<PostsWithUser>)
        swipeToRemove()
        coldStartDisabled()

    }

    private fun coldStartDisabled() {
        val sharedPreferences: SharedPreferences = context!!.getSharedPreferences(AppConstants.PREFS_FILENAME, 0)
        val editor = sharedPreferences.edit()
        editor.putBoolean(AppConstants.PREFS_COLD_START, false)
        editor.apply()
    }

    private fun displayPostRemovedSnackbar(post: PostsWithUser, position: Int) {
        val snackbar = Snackbar.make(
                    binding.container,
                    getString(R.string.post_removed_text),
                    Snackbar.LENGTH_LONG
        )
        val snanackbarRootView = snackbar.view

        // Change text color action button
        val snackbarActionButton = snanackbarRootView
                .findViewById<Button>(android.support.design.R.id.snackbar_action)
        snackbarActionButton.setTextColor(Color.YELLOW)

        // Set an action for snack bar
        snackbar.setAction(getString(R.string.undo_action).toUpperCase()) {
            mPostListAdapter!!.restoreItem(post, position)
        }
        snackbar.show()
    }

    private fun swipeToRemove() {
        val simpleItemTouchCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition

                if (direction == ItemTouchHelper.LEFT || direction == ItemTouchHelper.RIGHT) {
                    val post = mPostList!![position]
                    mPostListAdapter!!.removeItem(position)

                    displayPostRemovedSnackbar(post, position)

                }
            }

            override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
                val p = Paint()

                val icon = BitmapFactory.decodeResource(resources, R.drawable.ic_icon_remove)
                p.color = resources.getColor(R.color.colorRed, null)

                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

                    val itemView = viewHolder.itemView
                    val height = itemView.bottom.toFloat() - itemView.top.toFloat()
                    val width = height / 3

                    if (dX > 0) {
                        val background = RectF(itemView.left.toFloat(), itemView.top.toFloat(), dX, itemView.bottom.toFloat())
                        c.drawRect(background, p)
                        val icon_dest = RectF(itemView.left.toFloat() + width, itemView.top.toFloat() + width, itemView.left.toFloat() + 2 * width, itemView.bottom.toFloat() - width)
                        c.drawBitmap(icon, null, icon_dest, p)
                    } else {
                        val background = RectF(itemView.right.toFloat() + dX, itemView.top.toFloat(), itemView.right.toFloat(), itemView.bottom.toFloat())
                        c.drawRect(background, p)
                        val icon_dest = RectF(itemView.right.toFloat() - 2 * width, itemView.top.toFloat() + width, itemView.right.toFloat() - width, itemView.bottom.toFloat() - width)
                        c.drawBitmap(icon, null, icon_dest, p)
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }
        }
        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)
    }
}
