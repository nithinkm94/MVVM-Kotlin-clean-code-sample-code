package com.faustinogagneten.album.repository.util

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.support.annotation.MainThread
import android.support.annotation.WorkerThread

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
abstract class NetworkBoundResource<ResultType, RequestType> @MainThread
constructor(private val appExecutors: AppExecutors) {

    private val result = MediatorLiveData<Response<ResultType>>()

    init {
        result.value = Response.loading(null)
        //TODO:: Add method to check if data should be saved. This should apply for search data.
        val dbSource = loadFromDb()
        result.addSource(dbSource) { data ->
            result.removeSource(dbSource)
            if (shouldFetch(data)) {
                fetchFromNetwork(dbSource)
            } else {
                result.addSource(dbSource) { newData -> setValue(Response.success(newData)) }
            }
        }
    }

    @MainThread
    private fun setValue(newValue: Response<ResultType>) {
        if (!Objects.equals(result.value, newValue)) {
            result.value = newValue
        }
    }

    private fun fetchFromNetwork(dbSource: LiveData<ResultType>) {
        val apiResponse = createCall()
        // we re-attach dbSource as a new source, it will dispatch its latest value quickly
        result.addSource(dbSource) { newData -> setValue(Response.loading(newData)) }
        result.addSource(apiResponse) { response ->
            result.removeSource(apiResponse)
            result.removeSource(dbSource)

            if (response!!.isSuccessful) {
                appExecutors.diskIO().execute {
                    saveCallResult(processResponse(response)!!)
                    appExecutors.mainThread().execute {
                        // we specially request a new live data,
                        // otherwise we will get immediately last cached value,
                        // which may not be updated with latest results received from network.
                        result.addSource(loadFromDb()
                        ) { newData -> setValue(Response.success(newData)) }
                    }
                }
            } else {
                onFetchFailed()
                result.addSource(dbSource
                ) { newData -> setValue(Response.error(response.errorMessage!!, newData)) }
            }
        }
    }

    protected fun onFetchFailed() {}

    fun asLiveData(): LiveData<Response<ResultType>> {
        return result
    }

    @WorkerThread
    protected fun processResponse(response: ApiResponse<RequestType>): RequestType? {
        return response.body
    }

    @WorkerThread
    protected abstract fun saveCallResult(item: RequestType)

    @MainThread
    protected abstract fun shouldFetch(data: ResultType?): Boolean

    @MainThread
    protected abstract fun loadFromDb(): LiveData<ResultType>

    @MainThread
    protected abstract fun createCall(): LiveData<ApiResponse<RequestType>>
}
