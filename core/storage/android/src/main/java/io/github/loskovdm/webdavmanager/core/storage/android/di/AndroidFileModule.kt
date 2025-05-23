package io.github.loskovdm.webdavmanager.core.storage.android.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AndroidFileModule {

    @Provides
    @Singleton
    fun provideContext(@ApplicationContext context: Context): Context = context

}