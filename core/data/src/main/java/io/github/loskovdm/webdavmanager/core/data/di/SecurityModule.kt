package io.github.loskovdm.webdavmanager.core.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.loskovdm.webdavmanager.core.data.security.PasswordEncryptor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object SecurityModule {

    @Provides
    @Singleton
    fun providePasswordEncryptor(): PasswordEncryptor {
        return PasswordEncryptor()
    }

}