package com.example.webdavmanager.file_manager.data.remote

import com.example.webdavmanager.file_manager.data.model.WebDavConnectionInfo
import com.example.webdavmanager.file_manager.data.model.WebDavFile
import java.io.InputStream

interface WebDavFileApi {

    suspend fun getFileList(
        serverConnectionInfo: WebDavConnectionInfo,
        directoryUri: String
    ): Result<List<WebDavFile>>

    suspend fun uploadFile(
        serverConnectionInfo: WebDavConnectionInfo,
        fileStreamProvider: () -> InputStream,
        fileUri: String
    ): Result<Unit>

    suspend fun downloadFile(
        serverConnectionInfo: WebDavConnectionInfo,
        fileUri: String
    ): Result<InputStream>

    suspend fun moveFile(
        serverConnectionInfo: WebDavConnectionInfo,
        currentFileUri: String,
        destinationFileUri: String
    ): Result<Unit>

    suspend fun copyFile(
        serverConnectionInfo: WebDavConnectionInfo,
        currentFileUri: String,
        destinationFileUri: String
    ): Result<Unit>

    suspend fun deleteFile(
        serverConnectionInfo: WebDavConnectionInfo,
        fileUri: String
    ): Result<Unit>

    suspend fun createDirectory(
        serverConnectionInfo: WebDavConnectionInfo,
        directoryUri: String,
    ): Result<Unit>

}