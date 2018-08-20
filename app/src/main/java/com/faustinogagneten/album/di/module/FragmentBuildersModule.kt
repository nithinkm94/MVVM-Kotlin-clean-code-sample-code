package com.faustinogagneten.album.di.module

import com.faustinogagneten.album.view.post.fragments.PostDetailFragment
import com.faustinogagneten.album.view.post.fragments.PostsListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class FragmentBuildersModule {

    @ContributesAndroidInjector
    internal abstract fun contributePostsListFragment(): PostsListFragment

    @ContributesAndroidInjector
    internal abstract fun contributePostDetailFragment(): PostDetailFragment
}
