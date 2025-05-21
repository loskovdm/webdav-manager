package io.github.loskovdm.webdavmanager.file_manager.data.repository

import android.net.Uri
import io.github.loskovdm.webdavmanager.file_manager.data.model.WebDavConnectionInfo
import io.github.loskovdm.webdavmanager.file_manager.data.model.WebDavFile

interface FileManagerRepository {

    suspend fun setServerConnectionInfo(
        serverConnectionInfo: WebDavConnectionInfo
    ): Result<Unit>

    suspend fun getRemoteFileList(
        directoryUri: String
    ): Result<List<WebDavFile>>

    suspend fun uploadFile(
        remoteDirectoryUri: String,
        localFileUri: Uri,
        progressCallback: ((bytesUploaded: Long, totalBytes: Long) -> Unit)? = null
    ): Result<Unit>

    suspend fun downloadFile(
        remoteFile: WebDavFile,
        localDirectoryUri: Uri,
        progressCallback: ((bytesUploaded: Long, totalBytes: Long) -> Unit)? = null
    ): Result<Unit>

    suspend fun moveFile(
        remoteFileUri: String,
        remoteDestinationDirectoryUri: String
    ): Result<Unit>

    suspend fun copyFile(
        remoteFileUri: String,
        remoteDestinationDirectoryUri: String
    ): Result<Unit>

    suspend fun deleteFile(
        remoteFileUri: String
    ): Result<Unit>

    suspend fun createDirectory(
        remoteDestinationDirectoryUri: String,
        name: String
    ): Result<Unit>

    suspend fun renameFile(
        remoteFileUri: String,
        newName: String
    ): Result<Unit>

    suspend fun cacheFile(
        remoteFile: WebDavFile,
        progressCallback: ((bytesUploaded: Long, totalBytes: Long) -> Unit)? = null
    ): Result<Uri>

    suspend fun clearCache(): Result<Unit>

    suspend fun getLocalFileInfo(localFileUri: Uri): Result<Map<String, String?>>

}