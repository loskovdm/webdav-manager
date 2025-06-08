package io.github.loskovdm.webdavmanager.core.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.loskovdm.webdavmanager.core.data.repository.AndroidFileRepository
import io.github.loskovdm.webdavmanager.core.data.repository.AndroidFileRepositoryImpl
import io.github.loskovdm.webdavmanager.core.data.repository.FileManagerRepository
import io.github.loskovdm.webdavmanager.core.data.repository.FileManagerRepositoryImpl
import io.github.loskovdm.webdavmanager.core.data.repository.ServerRepository
import io.github.loskovdm.webdavmanager.core.data.repository.ServerRepositoryImpl
import io.github.loskovdm.webdavmanager.core.data.repository.WebDavFileRepository
import io.github.loskovdm.webdavmanager.core.data.repository.WebDavFileRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class DataModule {

    @Binds
    @Singleton
    abstract fun bindServerRepository(
        serverRepositoryImpl: ServerRepositoryImpl
    ): ServerRepository

    @Binds
    @Singleton
    abstract fun bindAndroidFileRepository(
        androidFileRepositoryImpl: AndroidFileRepositoryImpl
    ): AndroidFileRepository

    @Binds
    @Singleton
    abstract fun bindWebDavFileRepository(
        webDavFileRepositoryImpl: WebDavFileRepositoryImpl
    ): WebDavFileRepository

    @Binds
    @Singleton
    abstract fun bindFileManagerRepository(
        fileManagerRepositoryImpl: FileManagerRepositoryImpl
    ): FileManagerRepository

}