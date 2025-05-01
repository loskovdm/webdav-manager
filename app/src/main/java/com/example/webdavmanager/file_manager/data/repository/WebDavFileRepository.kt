package com.example.webdavmanager.file_manager.data.repository

import com.example.webdavmanager.file_manager.data.model.WebDavConnectionInfo
import com.example.webdavmanager.file_manager.data.model.WebDavFile
import java.io.InputStream

interface WebDavFileRepository {
    suspend fun setServerConnectionInfo(
        serverConnectionInfo: WebDavConnectionInfo
    ): Result<Unit>

    suspend fun getFileList(
        directoryPath: String
    ): Result<List<WebDavFile>>

    suspend fun uploadFile(
        fileStreamProvider: () -> InputStream,
        directoryPath: String,
        nameFile: String
    ): Result<Unit>

    suspend fun downloadFile(
        filePath: String
    ): Result<InputStream>

    suspend fun moveFile(
        filePath: String,
        destinationDirectoryPath: String
    ): Result<Unit>

    suspend fun copyFile(
        filePath: String,
        destinationDirectoryPath: String
    ): Result<Unit>

    suspend fun deleteFile(
        filePath: String
    ): Result<Unit>

    suspend fun createDirectory(
        destinationDirectoryPath: String,
        name: String
    ): Result<Unit>

    suspend fun renameFile(
        file: String,
        newName: String
    ): Result<Unit>
}