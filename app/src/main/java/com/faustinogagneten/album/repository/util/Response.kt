package com.faustinogagneten.album.repository.util

import com.faustinogagneten.album.repository.util.Status.*


/**
 * Created by fgagneten on 15/05/2017.
 * Generic class <T> which adds status information (success/error/loading) to LiveData response
</T> */
class Response<T>(val status: Status, val data: T?, val message: String?) {

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other == null || javaClass != other.javaClass) {
            return false
        }

        val response = other as Response<*>?

        if (status !== response!!.status) {
            return false
        }
        if (if (message != null) message != response!!.message else response!!.message != null) {
            return false
        }
        return if (data != null) data == response.data else response.data == null
    }

    override fun hashCode(): Int {
        var result = status.hashCode()
        result = 31 * result + (message?.hashCode() ?: 0)
        result = 31 * result + (data?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "Response{" +
                "status=" + status +
                ", message='" + message + '\''.toString() +
                ", data=" + data +
                '}'.toString()
    }

    companion object {

        fun <T> success(data: T?): Response<T> {
            return Response(SUCCESS, data, null)
        }

        fun <T> error(msg: String, data: T?): Response<T> {
            return Response(ERROR, data, msg)
        }

        fun <T> loading(data: T?): Response<T> {
            return Response(LOADING, data, null)
        }
    }
}
