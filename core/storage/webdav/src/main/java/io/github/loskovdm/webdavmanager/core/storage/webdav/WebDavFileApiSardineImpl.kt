package io.github.loskovdm.webdavmanager.core.storage.webdav

import android.util.Log
import io.github.loskovdm.webdavmanager.core.storage.webdav.model.WebDavConnectionInfo
import io.github.loskovdm.webdavmanager.core.storage.webdav.model.WebDavFile
import com.thegrizzlylabs.sardineandroid.InputStreamProvider
import com.thegrizzlylabs.sardineandroid.impl.OkHttpSardine
import io.github.loskovdm.webdavmanager.core.storage.webdav.mapper.asWebDavError
import io.github.loskovdm.webdavmanager.core.storage.webdav.model.asWebDavFile
import io.github.loskovdm.webdavmanager.core.storage.webdav.util.mapError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.InputStream
import javax.inject.Inject

internal class WebDavFileApiSardineImpl @Inject constructor(
    private val sardine: OkHttpSardine
) : WebDavFileApi {

    private var serverConnectionInfo = WebDavConnectionInfo("", "", "")

    override suspend fun setServerConnectionInfo(
        serverConnectionInfo: WebDavConnectionInfo
    ): Result<Unit> = runCatching {
        this.serverConnectionInfo = serverConnectionInfo
        sardine.setCredentials(
            serverConnectionInfo.user,
            serverConnectionInfo.password
        )
    }

    override suspend fun getFileList(
        directoryUri: String
    ): Result<List<WebDavFile>> = withContext(Dispatchers.IO) {
        runCatching {

            val resources = sardine.list(directoryUri)
            resources
                .filter { resource ->
                    val resourcePath = resource.path

                    val normalizedResourcePath = resourcePath.trimEnd('/')

                    val uri = directoryUri.substringAfter("://")
                    val path = "/" + uri.substringAfter("/", "")
                    val normalizedDirectoryPath = path.trimEnd('/')

                    normalizedResourcePath != normalizedDirectoryPath
                }
                .map { it.asWebDavFile(serverConnectionInfo.url) }
        }.mapError { it.asWebDavError() }
    }

    override suspend fun uploadFile(
        fileStreamProvider: () -> InputStream,
        fileUri: String,
        mimeType: String
    ): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching {
            val inputStreamProvider = object : InputStreamProvider {
                override fun getInputStream(): InputStream? {
                    return fileStreamProvider()
                }

                override fun getContentType(): MediaType? {
                    return mimeType.toMediaTypeOrNull()
                }
            }

            sardine.put(fileUri, inputStreamProvider)
        }.mapError { it.asWebDavError() }
    }

    override suspend fun downloadFile(
        fileUri: String
    ): Result<InputStream> = withContext(Dispatchers.IO) {
        runCatching {
            sardine.get(fileUri)
        }.mapError { it.asWebDavError() }
    }

    override suspend fun moveFile(
        currentFileUri: String,
        destinationFileUri: String
    ): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching {
            sardine.move(currentFileUri, destinationFileUri)
        }.mapError { it.asWebDavError() }
    }

    override suspend fun copyFile(
        currentFileUri: String,
        destinationFileUri: String
    ): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching {
            sardine.copy(currentFileUri, destinationFileUri)
        }.mapError { it.asWebDavError() }
    }

    override suspend fun deleteFile(
        fileUri: String
    ): Result<Unit> = withContext(Dispatchers.IO) {
        Log.d("loadTest", "Run delete file. File: $fileUri")
        runCatching {
            sardine.delete(fileUri)
        }.mapError { it.asWebDavError() }
    }

    override suspend fun createDirectory(
        directoryUri: String,
    ): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching {
            sardine.createDirectory(directoryUri)
        }.mapError { it.asWebDavError() }
    }
}