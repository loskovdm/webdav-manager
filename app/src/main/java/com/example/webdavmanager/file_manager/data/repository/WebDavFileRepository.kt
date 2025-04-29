package com.example.webdavmanager.file_manager.data.repository

import com.example.webdavmanager.file_manager.data.model.WebDavConnectionInfo
import com.example.webdavmanager.file_manager.data.model.WebDavFile
import java.io.InputStream

interface WebDavFileRepository {
    suspend fun getFileList(
        serverConnectionInfo: WebDavConnectionInfo,
        directoryPath: String
    ): Result<List<WebDavFile>>

    suspend fun uploadFile(
        serverConnectionInfo: WebDavConnectionInfo,
        fileStreamProvider: () -> InputStream,
        directoryPath: String,
        nameFile: String
    ): Result<Unit>

    suspend fun downloadFile(
        serverConnectionInfo: WebDavConnectionInfo,
        filePath: String
    ): Result<InputStream>

    suspend fun moveFile(
        serverConnectionInfo: WebDavConnectionInfo,
        filePath: String,
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

    suspend fun createDirectory(
        serverConnectionInfo: WebDavConnectionInfo,
        destinationDirectoryPath: String,
        name: String
    ): Result<Unit>

    suspend fun renameFile(
        serverConnectionInfo: WebDavConnectionInfo,
        file: String,
        newName: String
    ): Result<Unit>
}