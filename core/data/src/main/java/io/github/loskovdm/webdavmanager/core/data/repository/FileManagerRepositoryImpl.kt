package io.github.loskovdm.webdavmanager.core.data.repository

import android.net.Uri
import android.util.Log
import io.github.loskovdm.webdavmanager.core.model.FileModel
import io.github.loskovdm.webdavmanager.core.model.ServerModel
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class FileManagerRepositoryImpl @Inject constructor(
    private val androidFileRepository: AndroidFileRepository,
    private val webDavFileRepository: WebDavFileRepository
): FileManagerRepository {
    override suspend fun setServerConnectionInfo(server: ServerModel): Result<Unit> {
        return webDavFileRepository.setServerConnectionInfo(server)
    }

    override suspend fun getRemoteFileList(directoryUri: String): Result<List<FileModel>> {
        return webDavFileRepository.getFileList(directoryUri)
    }

    override suspend fun uploadFile(
        remoteDirectoryUri: String,
        localFileUri: Uri,
        progressCallback: ((bytesUploaded: Long, totalBytes: Long) -> Unit)?
    ): Result<Unit> {
        val fileInfo = androidFileRepository.getFileInfo(localFileUri)
        return fileInfo.fold(
            onSuccess = { info ->
                val name = info["name"] ?: return Result.failure(IllegalStateException("File name not found"))
                val mimeType = info["mimeType"] ?: "application/octet-stream"

                val fileStreamProvider = {
                    runBlocking {
                        androidFileRepository.readFile(localFileUri, progressCallback).getOrThrow()
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
        remoteFile: FileModel,
        localDirectoryUri: Uri,
        progressCallback: ((bytesUploaded: Long, totalBytes: Long) -> Unit)?
    ): Result<Unit> {
        val fileSize = remoteFile.size?.toLong() ?: -1L
        Log.d("loadTest", "WebDav file size: ${remoteFile.size}, converted: $fileSize")

        val fileStream = webDavFileRepository.downloadFile(remoteFile.uri)
        return fileStream.fold(
            onSuccess = { stream ->
                Log.d("loadTest", "Got stream, writing file with size: $fileSize")
                androidFileRepository.writeFile(
                    directoryUri = localDirectoryUri,
                    fileName = remoteFile.name,
                    mimeType = remoteFile.mimeType ?: "application/octet-stream",
                    fileStream = stream,
                    progressCallback = progressCallback,
                    fileSize = fileSize
                )
            },
            onFailure = { Result.failure(it) }
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

    override suspend fun cacheFile(remoteFile: FileModel, progressCallback: ((bytesUploaded: Long, totalBytes: Long) -> Unit)?): Result<Uri> {
        val fileStream = webDavFileRepository.downloadFile(remoteFile.uri)
        return androidFileRepository.cacheFile(
            fileName = remoteFile.name,
            fileSize = remoteFile.size?.toLong() ?: -1L,
            fileStream = fileStream.getOrThrow(),
            progressCallback = progressCallback
        )
    }

    override suspend fun getLocalFileInfo(localFileUri: Uri): Result<Map<String, String?>> {
        return androidFileRepository.getFileInfo(localFileUri)
    }

}