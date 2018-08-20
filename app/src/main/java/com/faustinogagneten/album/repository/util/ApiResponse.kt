package com.faustinogagneten.album.repository.util

import android.support.v4.util.ArrayMap
import retrofit2.Response
import java.io.IOException
import java.util.regex.Pattern

/**
 * Common class used by API responses.
 */

class ApiResponse<T> {
    val code: Int
    val body: T?
    val errorMessage: String?
    val links: MutableMap<String, String>

    val isSuccessful: Boolean
        get() = code >= 200 && code < 300

    constructor(error: Throwable) {
        code = 500
        body = null
        errorMessage = error.message
        links = ArrayMap<String, String>()
    }

    constructor(response: Response<T>) {
        code = response.code()
        if (response.isSuccessful) {
            body = response.body()
            errorMessage = null
        } else {
            var message: String? = null
            if (response.errorBody() != null) {
                try {
                    message = response.errorBody()!!.string()
                } catch (ignored: IOException) {
                }

            }
            if (message == null || message.trim { it <= ' ' }.length == 0) {
                message = response.message()
            }
            errorMessage = message
            body = null
        }
        val linkHeader = response.headers().get("link")
        if (linkHeader == null) {
            links = ArrayMap<String, String>()
        } else {
            links = ArrayMap()
            val matcher = LINK_PATTERN.matcher(linkHeader)

            while (matcher.find()) {
                val count = matcher.groupCount()
                if (count == 2) {
                    links[matcher.group(2)] = matcher.group(1)
                }
            }
        }
    }

    companion object {

        private val LINK_PATTERN = Pattern
                .compile("<([^>]*)>[\\s]*;[\\s]*rel=\"([a-zA-Z0-9]+)\"")
    }
}
