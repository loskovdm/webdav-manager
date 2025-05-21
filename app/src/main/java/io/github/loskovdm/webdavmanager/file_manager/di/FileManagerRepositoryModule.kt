package io.github.loskovdm.webdavmanager.file_manager.di

import io.github.loskovdm.webdavmanager.file_manager.data.repository.FileManagerRepository
import io.github.loskovdm.webdavmanager.file_manager.data.repository.FileManagerRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class FileManagerRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindFileManagerRepository(
        fileManagerRepositoryImpl: FileManagerRepositoryImpl
    ): FileManagerRepository

}