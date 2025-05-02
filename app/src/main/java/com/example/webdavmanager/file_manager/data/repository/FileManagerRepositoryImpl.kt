package com.example.webdavmanager.file_manager.data.repository

import android.net.Uri
import com.example.webdavmanager.file_manager.data.model.WebDavConnectionInfo
import com.example.webdavmanager.file_manager.data.model.WebDavFile
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class FileManagerRepositoryImpl @Inject constructor(
    private val androidFileRepository: AndroidFileRepository,
    private val webDavFileRepository: WebDavFileRepository
): FileManagerRepository {
    override suspend fun setServerConnectionInfo(serverConnectionInfo: WebDavConnectionInfo): Result<Unit> {
        return webDavFileRepository.setServerConnectionInfo(serverConnectionInfo)
    }

    override suspend fun getRemoteFileList(directoryUri: String): Result<List<WebDavFile>> {
        return webDavFileRepository.getFileList(directoryUri)
    }

    override suspend fun uploadFile(
        remoteDirectoryUri: String,
        localFileUri: Uri
    ): Result<Unit> {
        val fileInfo = androidFileRepository.getFileInfo(localFileUri)
        return fileInfo.fold(
            onSuccess = { info ->
                val name = info["name"] ?: return Result.failure(IllegalStateException("File name not found"))
                val mimeType = info["mimeType"] ?: "application/octet-stream"

                val fileStreamProvider = {
                    runBlocking {
                        androidFileRepository.readFile(localFileUri).getOrThrow()
                    }
                }

                return webDavFileRepository.uploadFile(
                    fileStreamProvider = fileStreamProvider,
                    directoryUri = remoteDirectoryUri,
                    fileName = name,
                    mimeType = mimeType
                )
            },
            onFailure = { Result.failure(it) }
        )
    }

    override suspend fun downloadFile(
        remoteFile: WebDavFile,
        localDirectoryUri: Uri
    ): Result<Unit> {
        val fileStream = webDavFileRepository.downloadFile(remoteFile.uri)
        return androidFileRepository.writeFile(
            directoryUri = localDirectoryUri,
            fileName = remoteFile.name,
            mimeType = remoteFile.mimeType ?: "application/octet-stream",
            fileStream = fileStream.getOrThrow()
        )
    }

    override suspend fun moveFile(
        remoteFileUri: String,
        remoteDestinationDirectoryUri: String
    ): Result<Unit> {
        return webDavFileRepository.moveFile(remoteFileUri, remoteDestinationDirectoryUri)
    }

    override suspend fun copyFile(
        remoteFileUri: String,
        remoteDestinationDirectoryUri: String
    ): Result<Unit> {
        return webDavFileRepository.copyFile(remoteFileUri, remoteDestinationDirectoryUri)
    }

    override suspend fun deleteFile(remoteFileUri: String): Result<Unit> {
        return webDavFileRepository.deleteFile(remoteFileUri)
    }

    override suspend fun createDirectory(
        remoteDestinationDirectoryUri: String,
        name: String
    ): Result<Unit> {
        return webDavFileRepository.createDirectory(remoteDestinationDirectoryUri, name)
    }

    override suspend fun renameFile(
        remoteFileUri: String,
        newName: String
    ): Result<Unit> {
        return webDavFileRepository.renameFile(remoteFileUri, newName)
    }

    override suspend fun cacheFile(remoteFile: WebDavFile): Result<Uri> {
        val fileStream = webDavFileRepository.downloadFile(remoteFile.uri)
        return androidFileRepository.cacheFile(
            fileName = remoteFile.name,
            mimeType = remoteFile.mimeType ?: "application/octet-stream",
            fileStream = fileStream.getOrThrow()
        )
    }

    override suspend fun clearCache(): Result<Unit> {
        return androidFileRepository.clearCache()
    }

}