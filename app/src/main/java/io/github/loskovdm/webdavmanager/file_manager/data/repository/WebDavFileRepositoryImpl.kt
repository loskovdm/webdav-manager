package io.github.loskovdm.webdavmanager.file_manager.data.repository

import io.github.loskovdm.webdavmanager.file_manager.data.model.WebDavConnectionInfo
import io.github.loskovdm.webdavmanager.file_manager.data.model.WebDavFile
import io.github.loskovdm.webdavmanager.file_manager.data.remote.WebDavFileDataSource
import java.io.InputStream
import java.net.URLEncoder
import javax.inject.Inject

class WebDavFileRepositoryImpl @Inject constructor(
    private val dataSource: WebDavFileDataSource
): WebDavFileRepository {

    override suspend fun setServerConnectionInfo(
        serverConnectionInfo: WebDavConnectionInfo
    ): Result<Unit> {
        return dataSource.setServerConnectionInfo(serverConnectionInfo)
    }

    override suspend fun getFileList(
        directoryUri: String
    ): Result<List<WebDavFile>> {
        return dataSource.getFileList(directoryUri)
    }

    override suspend fun uploadFile(
        fileStreamProvider: () -> InputStream,
        directoryUri: String,
        nameFile: String,
        mimeType: String
    ): Result<Unit> {
        return dataSource.uploadFile(fileStreamProvider, directoryUri + nameFile, mimeType)
    }

    override suspend fun downloadFile(
        fileUri: String
    ): Result<InputStream> {
        return dataSource.downloadFile(fileUri)
    }

    override suspend fun moveFile(
        fileUri: String,
        destinationDirectoryUri: String
    ): Result<Unit> {
        val destinationFileUri = destinationDirectoryUri + fileUri.substringAfterLast('/')
        return dataSource.moveFile(fileUri, destinationFileUri)
    }

    override suspend fun copyFile(
        fileUri: String,
        destinationDirectoryUri: String
    ): Result<Unit> {
        val destinationFileUri = destinationDirectoryUri + fileUri.substringAfterLast('/')
        return dataSource.copyFile(fileUri, destinationFileUri)
    }

    override suspend fun deleteFile(
        fileUri: String
    ): Result<Unit> {
        return dataSource.deleteFile(fileUri)
    }

    override suspend fun createDirectory(
        destinationDirectoryUri: String,
        name: String
    ): Result<Unit> {
        val directoryUri = destinationDirectoryUri + name
        return dataSource.createDirectory(directoryUri)
    }

    override suspend fun renameFile(
        fileUri: String,
        newName: String
    ): Result<Unit> = runCatching {
        val protocolAndHost = fileUri.substringBefore("://") + "://" +
                fileUri.substringAfter("://").substringBefore("/")
        val rawPath = fileUri.substringAfter(protocolAndHost)

        val parentPath = rawPath.trimEnd('/').substringBeforeLast("/")

        fun encodeSegment(segment: String) =
            URLEncoder
                .encode(segment, "UTF-8")
                .replace("+", "%20")

        val encodedParent = parentPath
            .split("/")
            .joinToString("/") { encodeSegment(it) }
        val encodedNewName = encodeSegment(newName)

        val newUrl = buildString {
            append(protocolAndHost)
            append(encodedParent)
            append("/")
            append(encodedNewName)
        }

        dataSource.moveFile(fileUri, newUrl)
    }

}