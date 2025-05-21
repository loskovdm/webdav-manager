package io.github.loskovdm.webdavmanager.file_manager.di

import io.github.loskovdm.webdavmanager.file_manager.data.remote.WebDavFileApiSardineImpl
import io.github.loskovdm.webdavmanager.file_manager.data.remote.WebDavFileApi
import io.github.loskovdm.webdavmanager.file_manager.data.repository.WebDavFileRepository
import io.github.loskovdm.webdavmanager.file_manager.data.repository.WebDavFileRepositoryImpl
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

    @Binds
    @Singleton
    abstract fun bindWebDavFileRepository(
        webDavFileRepositoryImpl: WebDavFileRepositoryImpl
    ): WebDavFileRepository

    companion object {
        @Provides
        @Singleton
        fun provideOkHttpSardine(): OkHttpSardine {
            return OkHttpSardine()
        }
    }

}