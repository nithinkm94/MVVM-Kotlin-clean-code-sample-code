package com.faustinogagneten.album

import android.app.Activity
import android.app.Application
import android.content.SharedPreferences
import com.faustinogagneten.album.di.AppInjector
import com.faustinogagneten.album.utils.AppConstants.PREFS_COLD_START
import com.faustinogagneten.album.utils.AppConstants.PREFS_FILENAME
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

/**
 * Created by fgagneten on 15/05/2017.
 * Main Application Class
 */

class AlbumApp : Application(), HasActivityInjector {

    @JvmField
    @Inject
    var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>? = null

    var sharedPreferences: SharedPreferences? = null

    override fun onCreate() {
        super.onCreate()

        AppInjector.init(this)
        coldStart()

    }

    override fun activityInjector(): AndroidInjector<Activity>? {
        return dispatchingAndroidInjector
    }

    /***
     * Cold start init
     */
    private fun coldStart() {
        sharedPreferences = this.getSharedPreferences(PREFS_FILENAME, 0)
        val editor = sharedPreferences!!.edit()
        editor.putBoolean(PREFS_COLD_START, true)
        editor.apply()
    }
}
