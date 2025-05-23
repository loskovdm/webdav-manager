package io.github.loskovdm.webdavmanager.core.storage.webdav

import io.github.loskovdm.webdavmanager.core.storage.webdav.model.WebDavConnectionInfo
import io.github.loskovdm.webdavmanager.core.storage.webdav.model.WebDavFile
import java.io.InputStream
import javax.inject.Inject

class WebDavFileDataSource @Inject constructor(private val api: WebDavFileApi) {
    suspend fun setServerConnectionInfo(serverConnectionInfo: WebDavConnectionInfo): Result<Unit> =
        api.setServerConnectionInfo(serverConnectionInfo)

    suspend fun getFileList(directoryUri: String): Result<List<WebDavFile>> =
        api.getFileList(directoryUri)

    suspend fun uploadFile(fileStreamProvider: () -> InputStream, fileUri: String, mimeType: String): Result<Unit> =
        api.uploadFile(fileStreamProvider, fileUri, mimeType)

    suspend fun downloadFile(fileUri: String): Result<InputStream> =
        api.downloadFile(fileUri)

    suspend fun moveFile(currentFileUri: String, destinationFileUri: String): Result<Unit> =
        api.moveFile(currentFileUri, destinationFileUri)

    suspend fun copyFile(currentFileUri: String, destinationFileUri: String): Result<Unit> =
        api.copyFile(currentFileUri, destinationFileUri)

    suspend fun deleteFile(fileUri: String): Result<Unit> =
        api.deleteFile(fileUri)

    suspend fun createDirectory(directoryUri: String): Result<Unit> =
        api.createDirectory(directoryUri)
}