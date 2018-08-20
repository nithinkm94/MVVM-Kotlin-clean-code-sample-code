package com.faustinogagneten.album.repository.util

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.support.annotation.MainThread

import com.faustinogagneten.album.utils.Objects


/**
 * A generic class that can provide a resource backed by both the sqlite database and the network.
 *
 *
 * You can read more about it in the [Architecture
 * Guide](https://developer.android.com/arch).
 *
 * @param <ResultType>
 * @param <RequestType>
</RequestType></ResultType> */
abstract class OfflineBoundResource<ResultType> @MainThread
constructor() {

    private val result = MediatorLiveData<Response<ResultType>>()

    init {
        result.value = Response.loading(null)
        //TODO:: Add method to check if data should be saved. This should apply for search data.
        val dbSource = loadFromDb()
        result.addSource(dbSource) {
            result.removeSource(dbSource)
            result.addSource(dbSource) { newData -> setValue(Response.success(newData)) }
        }
    }

    @MainThread
    private fun setValue(newValue: Response<ResultType>) {
        if (!Objects.equals(result.value, newValue)) {
            result.value = newValue
        }
    }

    fun asLiveData(): LiveData<Response<ResultType>> {
        return result
    }

    @MainThread
    protected abstract fun loadFromDb(): LiveData<ResultType>
}
