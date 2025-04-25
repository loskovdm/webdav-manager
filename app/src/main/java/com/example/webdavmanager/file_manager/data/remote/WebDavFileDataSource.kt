package com.example.webdavmanager.file_manager.data.remote

import com.example.webdavmanager.file_manager.data.model.WebDavConnectionInfo
import com.example.webdavmanager.file_manager.data.model.WebDavFile
import java.io.File
import java.io.InputStream
import javax.inject.Inject

class WebDavFileDataSource @Inject constructor(private val api: WebDavFileApi) {
    suspend fun getFileList(serverConnectionInfo: WebDavConnectionInfo, directoryPath: String): Result<List<WebDavFile>> =
        api.getFileList(serverConnectionInfo, directoryPath)

    suspend fun uploadFile(serverConnectionInfo: WebDavConnectionInfo, file: File, filePath: String): Result<Unit> =
        api.uploadFile(serverConnectionInfo, file, filePath)

    suspend fun downloadFile(serverConnectionInfo: WebDavConnectionInfo, filePath: String): Result<InputStream> =
        api.downloadFile(serverConnectionInfo, filePath)

    suspend fun moveFile(serverConnectionInfo: WebDavConnectionInfo, currentFilePath: String, destinationFilePath: String): Result<Unit> =
        api.moveFile(serverConnectionInfo, currentFilePath, destinationFilePath)

    suspend fun copyFile(serverConnectionInfo: WebDavConnectionInfo, currentFilePath: String, destinationFilePath: String): Result<Unit> =
        api.copyFile(serverConnectionInfo, currentFilePath, destinationFilePath)

    suspend fun deleteFile(serverConnectionInfo: WebDavConnectionInfo, filePath: String): Result<Unit> =
        api.deleteFile(serverConnectionInfo, filePath)

    suspend fun createDirectory(serverConnectionInfo: WebDavConnectionInfo, directoryPath: String): Result<Unit> =
        api.createDirectory(serverConnectionInfo, directoryPath)
}