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
    // TODO: Implement your own class for errors

    override suspend fun setServerConnectionInfo(
        serverConnectionInfo: WebDavConnectionInfo
    ): Result<Unit> = runCatching {
        sardine.setCredentials(
            serverConnectionInfo.user,
            serverConnectionInfo.password
        )
    }

    override suspend fun getFileList(
        directoryUri: String
    ): Result<List<WebDavFile>> = runCatching {
        val resources = sardine.list(directoryUri)
        resources.map { it.toWebDavFile() }
    }

    override suspend fun uploadFile(
        fileStreamProvider: () -> InputStream,
        fileUri: String,
        mimeType: String
    ): Result<Unit> = runCatching {
        val inputStreamProvider = object : InputStreamProvider {
            override fun getInputStream(): InputStream? {
                return fileStreamProvider()
            }

            override fun getContentType(): MediaType? {
                return mimeType.toMediaTypeOrNull()
            }
        }

        sardine.put(fileUri, inputStreamProvider)
    }

    override suspend fun downloadFile(
        fileUri: String
    ): Result<InputStream> = runCatching {
        sardine.get(fileUri)
    }

    override suspend fun moveFile(
        currentFileUri: String,
        destinationFileUri: String
    ): Result<Unit> = runCatching {
        sardine.move(currentFileUri, destinationFileUri)
    }

    override suspend fun copyFile(
        currentFileUri: String,
        destinationFileUri: String
    ): Result<Unit> = runCatching {
        sardine.copy(currentFileUri, destinationFileUri)
    }

    override suspend fun deleteFile(
        fileUri: String
    ): Result<Unit> = runCatching {
        sardine.delete(fileUri)
    }

    override suspend fun createDirectory(
        directoryUri: String,
    ): Result<Unit> = runCatching {
        sardine.createDirectory(directoryUri)
    }
}