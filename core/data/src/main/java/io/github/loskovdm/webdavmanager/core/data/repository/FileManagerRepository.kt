package io.github.loskovdm.webdavmanager.core.data.repository

import android.net.Uri
import io.github.loskovdm.webdavmanager.core.data.model.FileModel
import io.github.loskovdm.webdavmanager.core.data.model.ServerModel

interface FileManagerRepository {

    suspend fun setServerConnectionInfo(
        server: ServerModel
    ): Result<Unit>

    suspend fun getRemoteFileList(
        directoryUri: String
    ): Result<List<FileModel>>

    suspend fun uploadFile(
        remoteDirectoryUri: String,
        localFileUri: Uri,
        progressCallback: ((bytesUploaded: Long, totalBytes: Long) -> Unit)? = null
    ): Result<Unit>

    suspend fun downloadFile(
        remoteFile: FileModel,
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
        remoteFile: FileModel,
        progressCallback: ((bytesUploaded: Long, totalBytes: Long) -> Unit)? = null
    ): Result<Uri>

    suspend fun getLocalFileInfo(localFileUri: Uri): Result<Map<String, String?>>

}