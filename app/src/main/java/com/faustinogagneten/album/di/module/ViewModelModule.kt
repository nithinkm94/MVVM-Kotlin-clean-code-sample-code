package com.faustinogagneten.album.di.module

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider

import com.faustinogagneten.album.di.qualifires.ViewModelKey
import com.faustinogagneten.album.viewmodel.ViewModelFactory
import com.faustinogagneten.album.viewmodel.post.PostDetailViewModel
import com.faustinogagneten.album.viewmodel.post.PostsListViewModel

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created by fgagneten on 15/05/2017.
 * ViewModel module.
 */
@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(PostsListViewModel::class)
    internal abstract fun bindPostsListViewModel(postsListViewModel: PostsListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PostDetailViewModel::class)
    internal abstract fun bindPostDetailViewModel(postDetailViewModel: PostDetailViewModel): ViewModel

    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory
}
