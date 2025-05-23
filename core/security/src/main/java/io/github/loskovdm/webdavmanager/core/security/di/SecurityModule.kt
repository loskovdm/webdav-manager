package io.github.loskovdm.webdavmanager.core.security.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.loskovdm.webdavmanager.core.security.PasswordEncryptor
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