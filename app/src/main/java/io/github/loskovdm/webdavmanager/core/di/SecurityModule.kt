package io.github.loskovdm.webdavmanager.core.di

import io.github.loskovdm.webdavmanager.core.data.security.PasswordEncryptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SecurityModule {
    @Provides
    @Singleton
    fun providePasswordEncryptor(): PasswordEncryptor {
        return PasswordEncryptor()
    }
}