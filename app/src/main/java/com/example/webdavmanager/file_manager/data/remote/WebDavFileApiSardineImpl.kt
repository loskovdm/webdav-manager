package com.example.webdavmanager.file_manager.data.remote

import com.example.webdavmanager.file_manager.data.mapper.toWebDavFile
import com.example.webdavmanager.file_manager.data.model.WebDavConnectionInfo
import com.example.webdavmanager.file_manager.data.model.WebDavFile
import com.thegrizzlylabs.sardineandroid.InputStreamProvider
import com.thegrizzlylabs.sardineandroid.impl.OkHttpSardine
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.InputStream
import javax.inject.Inject

class WebDavFileApiSardineImpl @Inject constructor(
    private val sardine: OkHttpSardine
) : WebDavFileApi {
    private var currentServerConnectionInfo: WebDavConnectionInfo? = null

    // TODO: Implement your own class for errors

    override suspend fun getFileList(
        serverConnectionInfo: WebDavConnectionInfo,
        directoryUri: String
    ): Result<List<WebDavFile>> = runCatching {
        ensureCorrectCredentials(serverConnectionInfo)
        val resources = sardine.list(directoryUri)
        resources.map { it.toWebDavFile() }
    }

    override suspend fun uploadFile(
        serverConnectionInfo: WebDavConnectionInfo,
        fileStreamProvier: () -> InputStream,
        fileUri: String
    ): Result<Unit> = runCatching {
        ensureCorrectCredentials(serverConnectionInfo)

        val inputStreamProvider = object : InputStreamProvider {
            override fun getInputStream(): InputStream? {
                return fileStreamProvier()
            }

            override fun getContentType(): MediaType? {
                return "application/octet-stream".toMediaTypeOrNull()
            }
        }

        sardine.put(fileUri, inputStreamProvider)
    }

    override suspend fun downloadFile(
        serverConnectionInfo: WebDavConnectionInfo,
        fileUri: String
    ): Result<InputStream> = runCatching {
        ensureCorrectCredentials(serverConnectionInfo)
        sardine.get(fileUri)
    }

    override suspend fun moveFile(
        serverConnectionInfo: WebDavConnectionInfo,
        currentFileUri: String,
        destinationFileUri: String
    ): Result<Unit> = runCatching {
        ensureCorrectCredentials(serverConnectionInfo)
        sardine.move(currentFileUri, destinationFileUri)
    }

    override suspend fun copyFile(
        serverConnectionInfo: WebDavConnectionInfo,
        currentFileUri: String,
        destinationFileUri: String
    ): Result<Unit> = runCatching {
        ensureCorrectCredentials(serverConnectionInfo)
        sardine.copy(currentFileUri, destinationFileUri)
    }

    override suspend fun deleteFile(
        serverConnectionInfo: WebDavConnectionInfo,
        fileUri: String
    ): Result<Unit> = runCatching {
        ensureCorrectCredentials(serverConnectionInfo)
        sardine.delete(fileUri)
    }

    override suspend fun createDirectory(
        serverConnectionInfo: WebDavConnectionInfo,
        directoryUri: String,
    ): Result<Unit> = runCatching {
        ensureCorrectCredentials(serverConnectionInfo)
        sardine.createDirectory(directoryUri)
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