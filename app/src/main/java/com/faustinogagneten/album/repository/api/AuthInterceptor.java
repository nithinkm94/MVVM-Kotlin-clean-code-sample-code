package com.faustinogagneten.album.repository.api;

import android.support.annotation.NonNull;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * This class add information (API Key) to {@link okhttp3.OkHttpClient} which is passed in
 * {@link com.faustinogagneten.album.di.module.NetworkModule#okHttpClient(HttpLoggingInterceptor)}
 * which is required when making a request. This will ensure that all requests are made with the API key
 *
 */
public class AuthInterceptor implements Interceptor {

    /**
     * Default constructor.
     */
    public AuthInterceptor() {
    }


    /**
     * It is not required an API KEY for each request
     */
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        HttpUrl url = request.url().newBuilder()
                .build();
        request = request.newBuilder().url(url).build();
        return chain.proceed(request);
    }
}
