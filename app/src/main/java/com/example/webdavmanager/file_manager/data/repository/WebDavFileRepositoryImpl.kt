package com.example.webdavmanager.file_manager.data.repository

import com.example.webdavmanager.file_manager.data.model.WebDavConnectionInfo
import com.example.webdavmanager.file_manager.data.model.WebDavFile
import com.example.webdavmanager.file_manager.data.remote.WebDavFileDataSource
import java.io.InputStream
import javax.inject.Inject

class WebDavFileRepositoryImpl @Inject constructor(
    private val webDavFileDataSource: WebDavFileDataSource
): WebDavFileRepository {
    override suspend fun getFileList(
        serverConnectionInfo: WebDavConnectionInfo,
        directoryPath: String
    ): Result<List<WebDavFile>> {
        return webDavFileDataSource.getFileList(serverConnectionInfo, directoryPath)
    }

    override suspend fun uploadFile(
        serverConnectionInfo: WebDavConnectionInfo,
        fileStreamProvider: () -> InputStream,
        directoryPath: String,
        nameFile: String
    ): Result<Unit> {
        return webDavFileDataSource.uploadFile(serverConnectionInfo, fileStreamProvider, directoryPath + nameFile)
    }

    override suspend fun downloadFile(
        serverConnectionInfo: WebDavConnectionInfo,
        filePath: String
    ): Result<InputStream> {
        return webDavFileDataSource.downloadFile(serverConnectionInfo, filePath)
    }

    override suspend fun moveFile(
        serverConnectionInfo: WebDavConnectionInfo,
        filePath: String,
        destinationDirectoryPath: String
    ): Result<Unit> {
        val destinationFilePath = destinationDirectoryPath + filePath.substringAfterLast('/')
        return webDavFileDataSource.moveFile(serverConnectionInfo, filePath, destinationFilePath)
    }

    override suspend fun copyFile(
        serverConnectionInfo: WebDavConnectionInfo,
        filePath: String,
        destinationDirectoryPath: String
    ): Result<Unit> {
        val destinationFilePath = destinationDirectoryPath + filePath.substringAfterLast('/')
        return webDavFileDataSource.copyFile(serverConnectionInfo, filePath, destinationFilePath)
    }

    override suspend fun deleteFile(
        serverConnectionInfo: WebDavConnectionInfo,
        filePath: String
    ): Result<Unit> {
        return webDavFileDataSource.deleteFile(serverConnectionInfo, filePath)
    }

    override suspend fun createDirectory(
        serverConnectionInfo: WebDavConnectionInfo,
        destinationDirectoryPath: String,
        name: String
    ): Result<Unit> {
        val directoryPath = destinationDirectoryPath + name
        return webDavFileDataSource.createDirectory(serverConnectionInfo, directoryPath)
    }

    override suspend fun renameFile(
        serverConnectionInfo: WebDavConnectionInfo,
        filePath: String,
        newName: String
    ): Result<Unit> {
        val directoryPath: String
        val fileName: String
        if (filePath.endsWith('/')) {
            directoryPath = filePath.dropLast(1).substringBeforeLast('/') + "/"
            fileName = "$newName/"
        } else {
            directoryPath = filePath.substringBeforeLast('/') + "/"
            fileName = newName
        }
        val newPath = "$directoryPath$fileName"
        return webDavFileDataSource.moveFile(serverConnectionInfo, filePath, newPath)
    }
}