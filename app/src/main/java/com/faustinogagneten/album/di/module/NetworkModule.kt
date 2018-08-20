package com.faustinogagneten.album.di.module

import com.faustinogagneten.album.BuildConfig
import com.faustinogagneten.album.repository.api.AuthInterceptor
import com.faustinogagneten.album.repository.util.LiveDataCallAdapterFactory
import com.faustinogagneten.album.utils.AppConstants.CONNECT_TIMEOUT
import com.faustinogagneten.album.utils.AppConstants.READ_TIMEOUT
import com.faustinogagneten.album.utils.AppConstants.WRITE_TIMEOUT
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


/**
 * Created by fgagneten on 15/05/2017.
 */

@Module
class NetworkModule {


    /***
     * Provide information
     * @return
     */
    @Provides
    @Singleton
    internal fun provideOkHttpInterceptors(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.NONE else HttpLoggingInterceptor.Level.NONE)
    }

    /***
     * Basic OkHttp configuration
     * @param httpLoggingInterceptor
     * @return
     */
    @Provides
    @Singleton
    internal fun okHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {

        return OkHttpClient.Builder()
                .addInterceptor(AuthInterceptor())
                .addInterceptor(httpLoggingInterceptor)
                .connectTimeout(CONNECT_TIMEOUT.toLong(), TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT.toLong(), TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT.toLong(), TimeUnit.SECONDS)
                .build()
    }


    /***
     * Basic Retrofit configuration
     * @param okHttpClient
     * @return
     */
    @Provides
    @Singleton
    internal fun provideRetrofitClient(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .baseUrl(BuildConfig.API_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create()) // Serialize Objects
                .addCallAdapterFactory(LiveDataCallAdapterFactory())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) //Set call to return {@link Observable}
                .build()
    }
}
