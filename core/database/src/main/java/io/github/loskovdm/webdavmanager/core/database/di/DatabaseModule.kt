package io.github.loskovdm.webdavmanager.core.database.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.loskovdm.webdavmanager.core.database.ServerDao
import io.github.loskovdm.webdavmanager.core.database.ServerDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {

    @Provides
    @Singleton
    fun provideServerDatabase(@ApplicationContext context: Context): ServerDatabase {
        return ServerDatabase.Companion.getInstance(context)
    }

    @Provides
    @Singleton
    fun provideServerDao(database: ServerDatabase): ServerDao {
        return database.serverDao()
    }

}