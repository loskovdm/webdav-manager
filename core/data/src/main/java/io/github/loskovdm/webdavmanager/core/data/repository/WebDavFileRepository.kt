package io.github.loskovdm.webdavmanager.core.data.repository

import io.github.loskovdm.webdavmanager.core.data.model.File
import io.github.loskovdm.webdavmanager.core.data.model.Server
import java.io.InputStream

internal interface WebDavFileRepository {

    suspend fun setServerConnectionInfo(
        server: Server
    ): Result<Unit>

    suspend fun getFileList(
        directoryUri: String
    ): Result<List<File>>

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