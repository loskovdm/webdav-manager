package com.example.webdavmanager.core.di

import com.example.webdavmanager.core.data.security.PasswordEncryptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SecurityModel {
    @Provides
    @Singleton
    fun providePasswordEncryptor(): PasswordEncryptor {
        return PasswordEncryptor()
    }
}