package io.github.loskovdm.webdavmanager.core.di

import io.github.loskovdm.webdavmanager.core.data.repository.ServerRepository
import io.github.loskovdm.webdavmanager.core.data.repository.ServerRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ServerRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindServerRepository(
        serverRepositoryImpl: ServerRepositoryImpl
    ): ServerRepository

}