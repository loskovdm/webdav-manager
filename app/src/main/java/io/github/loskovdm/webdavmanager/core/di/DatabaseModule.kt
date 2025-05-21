package io.github.loskovdm.webdavmanager.core.di

import android.content.Context
import io.github.loskovdm.webdavmanager.core.data.local.ServerDao
import io.github.loskovdm.webdavmanager.core.data.local.ServerDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

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