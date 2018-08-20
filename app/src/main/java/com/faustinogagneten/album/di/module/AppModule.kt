package com.faustinogagneten.album.di.module

import android.app.Application
import android.content.SharedPreferences
import com.faustinogagneten.album.repository.api.AlbumService
import com.faustinogagneten.album.utils.AppConstants.PREFS_FILENAME
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton


@Module(includes = arrayOf(ViewModelModule::class, NetworkModule::class))
class AppModule {

    @Provides
    @Singleton
    internal fun provideAlbumService(retrofit: Retrofit): AlbumService {
        return retrofit.create(AlbumService::class.java)
    }

    @Provides
    @Singleton
    fun providePreference(app: Application): SharedPreferences = app.getSharedPreferences(PREFS_FILENAME, 0)

}
