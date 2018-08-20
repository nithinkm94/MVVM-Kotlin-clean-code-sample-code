package com.faustinogagneten.album.view

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.faustinogagneten.album.R
import com.faustinogagneten.album.databinding.ActivityMainBinding
import com.faustinogagneten.album.view.common.NavigationController
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject


class BaseActivity : AppCompatActivity(), HasSupportFragmentInjector {

    @JvmField
    @Inject
    var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>? = null

    @JvmField
    @Inject
    var navigationController: NavigationController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

        // Add project list fragment if this is first creation
        if (savedInstanceState == null) {
            navigationController!!.navigateToRepositoryListFragment()
        }


    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment>? {
        return dispatchingAndroidInjector
    }
}
