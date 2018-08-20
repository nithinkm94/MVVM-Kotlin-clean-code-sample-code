package com.faustinogagneten.album.utils

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.view.Window
import android.view.WindowManager
import com.faustinogagneten.album.R

/**
 *
 */

object DeviceUtils {

    fun setTranslucentStatusBar(window: Window?, color: Int) {
        if (window == null) return
        val sdkInt = Build.VERSION.SDK_INT
        if (sdkInt >= Build.VERSION_CODES.LOLLIPOP) {
            setTranslucentStatusBarLollipop(window, color)
        } else if (sdkInt >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatusBarKiKat(window)
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun setTranslucentStatusBarLollipop(window: Window, color: Int) {
        window.statusBarColor = window.context
                .resources
                .getColor(color)
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private fun setTranslucentStatusBarKiKat(window: Window) {
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    }

    fun getToolbarHeight(context: Context): Int {
        val styledAttributes = context.theme.obtainStyledAttributes(
                intArrayOf(R.attr.actionBarSize))
        val toolbarHeight = styledAttributes.getDimension(0, 0f).toInt()
        styledAttributes.recycle()

        return toolbarHeight
    }


}
