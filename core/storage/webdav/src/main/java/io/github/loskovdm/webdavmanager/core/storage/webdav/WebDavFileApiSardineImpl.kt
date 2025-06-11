package io.github.loskovdm.webdavmanager.core.storage.webdav

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
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
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
            sardine.copy(encodeFileUri(currentFileUri), encodeFileUri(destinationFileUri))
        }.mapError { it.asWebDavError() }
    }

    override suspend fun deleteFile(
        fileUri: String
    ): Result<Unit> = withContext(Dispatchers.IO) {
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

    private fun encodeFileUri(fileUri: String): String {
        try {
            // Parse the URI parts manually to avoid constructor exception
            val schemeDelimiter = "://"
            val schemeIndex = fileUri.indexOf(schemeDelimiter)

            if (schemeIndex == -1) return fileUri

            val scheme = fileUri.substring(0, schemeIndex)
            val remaining = fileUri.substring(schemeIndex + schemeDelimiter.length)

            // Split authority and path
            val pathIndex = remaining.indexOf('/', 0)
            if (pathIndex == -1) return fileUri

            val authority = remaining.substring(0, pathIndex)
            val path = remaining.substring(pathIndex)

            // Encode each path segment
            val encodedPath = path.split("/").joinToString("/") { segment ->
                if (segment.isEmpty()) {
                    ""
                } else {
                    URLEncoder.encode(segment, StandardCharsets.UTF_8.name())
                        .replace("+", "%20")
                }
            }

            return "$scheme://$authority$encodedPath"
        } catch (_: Exception) {
            return fileUri
        }
    }
}