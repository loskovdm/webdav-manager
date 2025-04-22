package com.example.webdavmanager.file_manager.data.remote

import com.example.webdavmanager.file_manager.data.model.WebDavConnectionInfo
import com.example.webdavmanager.file_manager.data.model.WebDavFile

interface WebDavFileApi {

    suspend fun getFileList(
        serverConnectionInfo: WebDavConnectionInfo,
        directoryPath: String
    ): Result<List<WebDavFile>>

    suspend fun uploadFile(
        serverConnectionInfo: WebDavConnectionInfo,
        localFilePath: String,
        remoteDirectoryPath: String
    ): Result<Unit>

    suspend fun downloadFile(
        serverConnectionInfo: WebDavConnectionInfo,
        remoteFilePath: String,
        localDirectoryPath: String
    ): Result<Unit>

    suspend fun moveFile(
        serverConnectionInfo: WebDavConnectionInfo,
        currentFilePath: String,
        destinationDirectoryPath: String
    ): Result<Unit>

    suspend fun copyFile(
        serverConnectionInfo: WebDavConnectionInfo,
        filePath: String,
        destinationDirectoryPath: String
    ): Result<Unit>

    suspend fun deleteFile(
        serverConnectionInfo: WebDavConnectionInfo,
        filePath: String
    ): Result<Unit>

    suspend fun renameFile(
        serverConnectionInfo: WebDavConnectionInfo,
        filePath: String,
        newName: String
    ): Result<Unit>

    suspend fun createDirectory(
        serverConnectionInfo: WebDavConnectionInfo,
        directoryPath: String
    ): Result<Unit>

}