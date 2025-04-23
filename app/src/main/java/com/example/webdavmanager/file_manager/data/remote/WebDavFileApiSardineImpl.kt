package com.example.webdavmanager.file_manager.data.remote

import com.example.webdavmanager.file_manager.data.mapper.toWebDavFile
import com.example.webdavmanager.file_manager.data.model.WebDavConnectionInfo
import com.example.webdavmanager.file_manager.data.model.WebDavFile
import com.thegrizzlylabs.sardineandroid.impl.OkHttpSardine
import javax.inject.Inject

class WebDavFileApiSardineImpl @Inject constructor(
    private val sardine: OkHttpSardine
) : WebDavFileApi {
    private var currentServerConnectionInfo: WebDavConnectionInfo? = null

    override suspend fun getFileList(
        serverConnectionInfo: WebDavConnectionInfo,
        directoryPath: String
    ): Result<List<WebDavFile>> = runCatching {
        ensureCorrectCredentials(serverConnectionInfo)
        val resources = sardine.list(directoryPath)
        resources.map { it.toWebDavFile() }
    }

    override suspend fun uploadFile(
        serverConnectionInfo: WebDavConnectionInfo,
        fileName: String,
        fileContent: ByteArray,
        remoteDirectoryPath: String
    ): Result<Unit> = runCatching {
        ensureCorrectCredentials(serverConnectionInfo)
        sardine.put(remoteDirectoryPath + fileName, fileContent)
    }

    override suspend fun downloadFile(
        serverConnectionInfo: WebDavConnectionInfo,
        remoteFilePath: String
    ): Result<ByteArray> = runCatching {
        ensureCorrectCredentials(serverConnectionInfo)
        val inputStream = sardine.get(remoteFilePath)
        inputStream.use { it.readBytes() }
    }

    override suspend fun moveFile(
        serverConnectionInfo: WebDavConnectionInfo,
        currentFilePath: String,
        destinationDirectoryPath: String
    ): Result<Unit> = runCatching {
        ensureCorrectCredentials(serverConnectionInfo)
        sardine.move(currentFilePath, destinationDirectoryPath + currentFilePath.substringAfterLast('/'))
    }

    override suspend fun copyFile(
        serverConnectionInfo: WebDavConnectionInfo,
        filePath: String,
        destinationDirectoryPath: String
    ): Result<Unit> = runCatching {
        ensureCorrectCredentials(serverConnectionInfo)
        sardine.copy(filePath, destinationDirectoryPath + filePath.substringAfterLast('/'))
    }

    override suspend fun deleteFile(
        serverConnectionInfo: WebDavConnectionInfo,
        filePath: String
    ): Result<Unit> =runCatching {
        ensureCorrectCredentials(serverConnectionInfo)
        sardine.delete(filePath)
    }

    override suspend fun renameFile(
        serverConnectionInfo: WebDavConnectionInfo,
        filePath: String,
        newName: String
    ): Result<Unit> = runCatching {
        ensureCorrectCredentials(serverConnectionInfo)
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
        sardine.move(filePath, newPath)
    }

    override suspend fun createDirectory(
        serverConnectionInfo: WebDavConnectionInfo,
        destinationDirectoryPath: String,
        directoryName: String
    ): Result<Unit> = runCatching {
        ensureCorrectCredentials(serverConnectionInfo)
        val directoryPath = "$destinationDirectoryPath$directoryName/"
        sardine.createDirectory(directoryPath)
    }

    private fun ensureCorrectCredentials(serverConnectionInfo: WebDavConnectionInfo) {
        if (serverConnectionInfo != currentServerConnectionInfo) {
            setCredentials(serverConnectionInfo)
        }
    }

    private fun setCredentials(serverConnectionInfo: WebDavConnectionInfo) {
        sardine.setCredentials(serverConnectionInfo.user, serverConnectionInfo.password)
        currentServerConnectionInfo = serverConnectionInfo
    }
}