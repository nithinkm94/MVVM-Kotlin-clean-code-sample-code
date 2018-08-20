package com.faustinogagneten.album.di.module

import com.faustinogagneten.album.view.BaseActivity

import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class MainActivityModule {
    @ContributesAndroidInjector(modules = arrayOf(FragmentBuildersModule::class))
    internal abstract fun contributeMainActivity(): BaseActivity

}
