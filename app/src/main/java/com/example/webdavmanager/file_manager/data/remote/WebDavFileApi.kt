package com.example.webdavmanager.file_manager.data.remote

import com.example.webdavmanager.file_manager.data.model.WebDavConnectionInfo
import com.example.webdavmanager.file_manager.data.model.WebDavFile
import java.io.File
import java.io.InputStream

interface WebDavFileApi {

    suspend fun getFileList(
        serverConnectionInfo: WebDavConnectionInfo,
        directoryPath: String
    ): Result<List<WebDavFile>>

    suspend fun uploadFile(
        serverConnectionInfo: WebDavConnectionInfo,
        file: File,
        filePath: String
    ): Result<Unit>

    suspend fun downloadFile(
        serverConnectionInfo: WebDavConnectionInfo,
        filePath: String
    ): Result<InputStream>

    suspend fun moveFile(
        serverConnectionInfo: WebDavConnectionInfo,
        currentFilePath: String,
        destinationFilePath: String
    ): Result<Unit>

    suspend fun copyFile(
        serverConnectionInfo: WebDavConnectionInfo,
        currentFilePath: String,
        destinationFilePath: String
    ): Result<Unit>

    suspend fun deleteFile(
        serverConnectionInfo: WebDavConnectionInfo,
        filePath: String
    ): Result<Unit>

    suspend fun createDirectory(
        serverConnectionInfo: WebDavConnectionInfo,
        directoryPath: String,
    ): Result<Unit>

}