package com.example.webdavmanager.file_manager.data.remote

import com.example.webdavmanager.file_manager.data.mapper.toWebDavFile
import com.example.webdavmanager.file_manager.data.model.WebDavConnectionInfo
import com.example.webdavmanager.file_manager.data.model.WebDavFile
import com.thegrizzlylabs.sardineandroid.impl.OkHttpSardine
import java.io.File
import java.io.InputStream
import javax.inject.Inject

class WebDavFileApiSardineImpl @Inject constructor(
    private val sardine: OkHttpSardine
) : WebDavFileApi {
    private var currentServerConnectionInfo: WebDavConnectionInfo? = null

    // TODO: Implement your own class for errors

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
        file: File,
        filePath: String
    ): Result<Unit> = runCatching {
        ensureCorrectCredentials(serverConnectionInfo)
        sardine.put(filePath, file, "application/octet-stream")
    }

    override suspend fun downloadFile(
        serverConnectionInfo: WebDavConnectionInfo,
        filePath: String
    ): Result<InputStream> = runCatching {
        ensureCorrectCredentials(serverConnectionInfo)
        sardine.get(filePath)
    }

    override suspend fun moveFile(
        serverConnectionInfo: WebDavConnectionInfo,
        currentFilePath: String,
        destinationFilePath: String
    ): Result<Unit> = runCatching {
        ensureCorrectCredentials(serverConnectionInfo)
        sardine.move(currentFilePath, destinationFilePath)
    }

    override suspend fun copyFile(
        serverConnectionInfo: WebDavConnectionInfo,
        currentFilePath: String,
        destinationFilePath: String
    ): Result<Unit> = runCatching {
        ensureCorrectCredentials(serverConnectionInfo)
        sardine.copy(currentFilePath, destinationFilePath)
    }

    override suspend fun deleteFile(
        serverConnectionInfo: WebDavConnectionInfo,
        filePath: String
    ): Result<Unit> = runCatching {
        ensureCorrectCredentials(serverConnectionInfo)
        sardine.delete(filePath)
    }

    override suspend fun createDirectory(
        serverConnectionInfo: WebDavConnectionInfo,
        directoryPath: String,
    ): Result<Unit> = runCatching {
        ensureCorrectCredentials(serverConnectionInfo)
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