package com.example.webdavmanager.file_manager.data.repository

import com.example.webdavmanager.file_manager.data.model.WebDavConnectionInfo
import com.example.webdavmanager.file_manager.data.model.WebDavFile
import com.example.webdavmanager.file_manager.data.remote.WebDavFileDataSource
import java.io.InputStream
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
        nameFile: String
    ): Result<Unit> {
        return dataSource.uploadFile(fileStreamProvider, directoryUri + nameFile)
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
    ): Result<Unit> {
        val directoryUri: String
        val fileName: String
        if (fileUri.endsWith('/')) {
            directoryUri = fileUri.dropLast(1).substringBeforeLast('/') + "/"
            fileName = "$newName/"
        } else {
            directoryUri = fileUri.substringBeforeLast('/') + "/"
            fileName = newName
        }
        val newUri = "$directoryUri$fileName"
        return dataSource.moveFile(fileUri, newUri)
    }

}