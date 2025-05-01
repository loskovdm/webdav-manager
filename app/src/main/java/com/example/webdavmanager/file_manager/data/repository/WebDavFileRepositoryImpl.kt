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
        directoryPath: String
    ): Result<List<WebDavFile>> {
        return dataSource.getFileList(directoryPath)
    }

    override suspend fun uploadFile(
        fileStreamProvider: () -> InputStream,
        directoryPath: String,
        nameFile: String
    ): Result<Unit> {
        return dataSource.uploadFile(fileStreamProvider, directoryPath + nameFile)
    }

    override suspend fun downloadFile(
        filePath: String
    ): Result<InputStream> {
        return dataSource.downloadFile(filePath)
    }

    override suspend fun moveFile(
        filePath: String,
        destinationDirectoryPath: String
    ): Result<Unit> {
        val destinationFilePath = destinationDirectoryPath + filePath.substringAfterLast('/')
        return dataSource.moveFile(filePath, destinationFilePath)
    }

    override suspend fun copyFile(
        filePath: String,
        destinationDirectoryPath: String
    ): Result<Unit> {
        val destinationFilePath = destinationDirectoryPath + filePath.substringAfterLast('/')
        return dataSource.copyFile(filePath, destinationFilePath)
    }

    override suspend fun deleteFile(
        filePath: String
    ): Result<Unit> {
        return dataSource.deleteFile(filePath)
    }

    override suspend fun createDirectory(
        destinationDirectoryPath: String,
        name: String
    ): Result<Unit> {
        val directoryPath = destinationDirectoryPath + name
        return dataSource.createDirectory(directoryPath)
    }

    override suspend fun renameFile(
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
        return dataSource.moveFile(filePath, newPath)
    }

}