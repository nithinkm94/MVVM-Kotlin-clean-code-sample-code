package com.faustinogagneten.album.di.component

import android.app.Application
import com.faustinogagneten.album.AlbumApp
import com.faustinogagneten.album.di.module.AppModule
import com.faustinogagneten.album.di.module.MainActivityModule
import com.faustinogagneten.album.di.module.RoomModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton


@Singleton
@Component(modules = arrayOf(AndroidInjectionModule::class, AppModule::class, MainActivityModule::class, RoomModule::class))
interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun inject(albumApp: AlbumApp)
}
