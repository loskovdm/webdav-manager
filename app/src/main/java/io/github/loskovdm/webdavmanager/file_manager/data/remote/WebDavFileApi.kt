package io.github.loskovdm.webdavmanager.file_manager.data.remote

import io.github.loskovdm.webdavmanager.file_manager.data.model.WebDavConnectionInfo
import io.github.loskovdm.webdavmanager.file_manager.data.model.WebDavFile
import java.io.InputStream

interface WebDavFileApi {

    suspend fun setServerConnectionInfo(
        serverConnectionInfo: WebDavConnectionInfo
    ): Result<Unit>

    suspend fun getFileList(
        directoryUri: String
    ): Result<List<WebDavFile>>

    suspend fun uploadFile(
        fileStreamProvider: () -> InputStream,
        fileUri: String,
        mimeType: String
    ): Result<Unit>

    suspend fun downloadFile(
        fileUri: String
    ): Result<InputStream>

    suspend fun moveFile(
        currentFileUri: String,
        destinationFileUri: String
    ): Result<Unit>

    suspend fun copyFile(
        currentFileUri: String,
        destinationFileUri: String
    ): Result<Unit>

    suspend fun deleteFile(
        fileUri: String
    ): Result<Unit>

    suspend fun createDirectory(
        directoryUri: String,
    ): Result<Unit>

}