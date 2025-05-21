package io.github.loskovdm.webdavmanager.file_manager.di

import android.content.Context
import io.github.loskovdm.webdavmanager.file_manager.data.repository.AndroidFileRepository
import io.github.loskovdm.webdavmanager.file_manager.data.repository.AndroidFileRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AndroidFileModule {

    @Binds
    @Singleton
    abstract fun bindAndroidFileRepository(
        androidFileRepositoryImpl: AndroidFileRepositoryImpl
    ): AndroidFileRepository

    companion object{
        @Provides
        @Singleton
        fun provideContext(@ApplicationContext context: Context): Context = context
    }

}