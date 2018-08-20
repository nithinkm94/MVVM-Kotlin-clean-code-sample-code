package com.faustinogagneten.album.view.common

import android.support.v4.app.FragmentManager
import com.faustinogagneten.album.R
import com.faustinogagneten.album.view.BaseActivity
import com.faustinogagneten.album.view.post.fragments.PostDetailFragment
import com.faustinogagneten.album.view.post.fragments.PostsListFragment
import javax.inject.Inject

/**
 * A utility class that handles navigation in [BaseActivity].
 */

class NavigationController @Inject
constructor(baseActivity: BaseActivity) {
    private val containerId: Int
    private val fragmentManager: FragmentManager

    init {
        containerId = R.id.container
        fragmentManager = baseActivity.supportFragmentManager
    }

    fun navigateToRepositoryListFragment() {
        val fragment = PostsListFragment()
        fragmentManager.beginTransaction()
                .add(containerId, fragment)
                .commitAllowingStateLoss()
    }

    fun navigateToDetailFragment(postId: Int) {
        val fragment = PostDetailFragment.newInstance(postId)

        fragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .replace(containerId, fragment)
                .addToBackStack(null)
                .commitAllowingStateLoss()
    }
}
