package com.example.webdavmanager.file_manager.di

import com.example.webdavmanager.file_manager.data.remote.WebDavFileApiSardineImpl
import com.example.webdavmanager.file_manager.data.remote.WebDavFileApi
import com.thegrizzlylabs.sardineandroid.impl.OkHttpSardine
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class WebDavModule {

    @Binds
    @Singleton
    abstract fun bindWebDavFileApi(webDavFileApiSardineImpl: WebDavFileApiSardineImpl): WebDavFileApi

    companion object {
        @Provides
        @Singleton
        fun provideOkHttpSardine(): OkHttpSardine {
            return OkHttpSardine()
        }
    }

}