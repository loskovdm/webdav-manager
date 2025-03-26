package com.example.webdavmanager.core.di

import android.content.Context
import com.example.webdavmanager.core.data.local.ServerDao
import com.example.webdavmanager.core.data.local.ServerDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModel {

    @Provides
    @Singleton
    fun provideServerDatabase(@ApplicationContext context: Context): ServerDatabase {
        return ServerDatabase.getInstance(context)
    }

    @Provides
    @Singleton
    fun provideServerDao(database: ServerDatabase): ServerDao {
        return database.serverDao()
    }
}