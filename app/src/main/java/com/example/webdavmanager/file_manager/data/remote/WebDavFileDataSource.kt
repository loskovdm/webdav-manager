package com.example.webdavmanager.file_manager.data.remote

import com.example.webdavmanager.file_manager.data.model.WebDavConnectionInfo
import com.example.webdavmanager.file_manager.data.model.WebDavFile
import java.io.InputStream
import javax.inject.Inject

class WebDavFileDataSource @Inject constructor(private val api: WebDavFileApi) {
    suspend fun getFileList(serverConnectionInfo: WebDavConnectionInfo, directoryUri: String): Result<List<WebDavFile>> =
        api.getFileList(serverConnectionInfo, directoryUri)

    suspend fun uploadFile(serverConnectionInfo: WebDavConnectionInfo, fileStreamProvider: () -> InputStream, fileUri: String): Result<Unit> =
        api.uploadFile(serverConnectionInfo, fileStreamProvider, fileUri)

    suspend fun downloadFile(serverConnectionInfo: WebDavConnectionInfo, fileUri: String): Result<InputStream> =
        api.downloadFile(serverConnectionInfo, fileUri)

    suspend fun moveFile(serverConnectionInfo: WebDavConnectionInfo, currentFileUri: String, destinationFileUri: String): Result<Unit> =
        api.moveFile(serverConnectionInfo, currentFileUri, destinationFileUri)

    suspend fun copyFile(serverConnectionInfo: WebDavConnectionInfo, currentFileUri: String, destinationFileUri: String): Result<Unit> =
        api.copyFile(serverConnectionInfo, currentFileUri, destinationFileUri)

    suspend fun deleteFile(serverConnectionInfo: WebDavConnectionInfo, fileUri: String): Result<Unit> =
        api.deleteFile(serverConnectionInfo, fileUri)

    suspend fun createDirectory(serverConnectionInfo: WebDavConnectionInfo, directoryUri: String): Result<Unit> =
        api.createDirectory(serverConnectionInfo, directoryUri)
}