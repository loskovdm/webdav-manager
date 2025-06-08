package io.github.loskovdm.webdavmanager.core.data.repository

import android.net.Uri
import io.github.loskovdm.webdavmanager.core.data.model.File
import io.github.loskovdm.webdavmanager.core.data.model.Server

interface FileManagerRepository {

    suspend fun setServerConnectionInfo(
        server: Server
    ): Result<Unit>

    suspend fun getRemoteFileList(
        directoryUri: String
    ): Result<List<File>>

    suspend fun uploadFile(
        remoteDirectoryUri: String,
        localFileUri: Uri,
        progressCallback: ((bytesUploaded: Long, totalBytes: Long) -> Unit)? = null
    ): Result<Unit>

    suspend fun downloadFile(
        remoteFile: File,
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
        remoteFile: File,
        progressCallback: ((bytesUploaded: Long, totalBytes: Long) -> Unit)? = null
    ): Result<Uri>

    suspend fun getLocalFileInfo(localFileUri: Uri): Result<Map<String, String?>>

}