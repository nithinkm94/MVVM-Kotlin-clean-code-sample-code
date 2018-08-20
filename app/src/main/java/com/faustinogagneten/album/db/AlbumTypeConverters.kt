package com.faustinogagneten.album.db

import android.annotation.SuppressLint
import android.arch.persistence.room.TypeConverter
import android.arch.persistence.room.util.StringUtil
import java.util.Collections.emptyList

@SuppressLint("RestrictedApi")
object AlbumTypeConverters {
    @TypeConverter
    fun stringToIntList(data: String?): List<Int>? {
        return if (data == null) {
            emptyList<Int>()
        } else StringUtil.splitToIntList(data)
    }

    @TypeConverter
    fun intListToString(ints: List<Int>): String? {
        return StringUtil.joinIntoString(ints)
    }
}
