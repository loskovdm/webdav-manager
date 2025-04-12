package com.example.webdavmanager.file_manager.di

import com.example.webdavmanager.file_manager.data.remote.WebDavFileApiSardineImpl
import com.example.webdavmanager.file_manager.data.remote.WebDavFileApi
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class WebDavModule {

    @Binds
    @Singleton
    abstract fun bindWebDavFileApi(webDavFileApiSardineImpl: WebDavFileApiSardineImpl): WebDavFileApi

}