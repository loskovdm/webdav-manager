package io.github.loskovdm.webdavmanager.core.storage.webdav.di

import com.thegrizzlylabs.sardineandroid.impl.OkHttpSardine
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.loskovdm.webdavmanager.core.storage.webdav.WebDavFileApi
import io.github.loskovdm.webdavmanager.core.storage.webdav.WebDavFileApiSardineImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class SardineModule {

    companion object {
        @Provides
        @Singleton
        fun provideOkHttpSardine(): OkHttpSardine {
            return OkHttpSardine()
        }
    }

    @Binds
    @Singleton
    abstract fun bindWebDavFileApi(
        webDavFileApiSardineImpl: WebDavFileApiSardineImpl
    ): WebDavFileApi

}