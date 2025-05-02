package com.example.webdavmanager.file_manager.data.repository

import com.example.webdavmanager.file_manager.data.model.WebDavConnectionInfo
import com.example.webdavmanager.file_manager.data.model.WebDavFile
import java.io.InputStream

interface WebDavFileRepository {
    suspend fun setServerConnectionInfo(
        serverConnectionInfo: WebDavConnectionInfo
    ): Result<Unit>

    suspend fun getFileList(
        directoryUri: String
    ): Result<List<WebDavFile>>

    suspend fun uploadFile(
        fileStreamProvider: () -> InputStream,
        directoryUri: String,
        fileName: String,
        mimeType: String
    ): Result<Unit>

    suspend fun downloadFile(
        fileUri: String
    ): Result<InputStream>

    suspend fun moveFile(
        fileUri: String,
        destinationDirectoryUri: String
    ): Result<Unit>

    suspend fun copyFile(
        fileUri: String,
        destinationDirectoryUri: String
    ): Result<Unit>

    suspend fun deleteFile(
        fileUri: String
    ): Result<Unit>

    suspend fun createDirectory(
        destinationDirectoryUri: String,
        name: String
    ): Result<Unit>

    suspend fun renameFile(
        file: String,
        newName: String
    ): Result<Unit>
}