package com.example.webdavmanager.server_list.di

import com.example.webdavmanager.server_list.domain.repository.ServerListRepository
import com.example.webdavmanager.server_list.domain.use_cases.DeleteServerUseCase
import com.example.webdavmanager.server_list.domain.use_cases.GetServersUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object ServerListUseCaseModule {

    @Provides
    fun provideGetServersUseCase(repository: ServerListRepository): GetServersUseCase {
        return GetServersUseCase(repository)
    }

    @Provides
    fun provideDeleteServerUseCase(repository: ServerListRepository): DeleteServerUseCase {
        return DeleteServerUseCase(repository)
    }

}