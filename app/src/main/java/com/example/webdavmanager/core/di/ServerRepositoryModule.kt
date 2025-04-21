package com.example.webdavmanager.core.di

import com.example.webdavmanager.core.data.repository.ServerRepository
import com.example.webdavmanager.server_list.domain.repository.ServerListRepository
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
    abstract fun bindServerListRepository(
        serverRepository: ServerRepository
    ): ServerListRepository

}