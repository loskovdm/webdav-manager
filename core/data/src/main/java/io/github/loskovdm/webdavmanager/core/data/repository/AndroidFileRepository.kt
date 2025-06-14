package io.github.loskovdm.webdavmanager.core.data.repository

import android.net.Uri
import java.io.InputStream

internal interface AndroidFileRepository {

    suspend fun readFile(
        uri: Uri,
        progressCallback: ((bytesRead: Long, totalBytes: Long) -> Unit)? = null
    ): Result<InputStream>

    suspend fun writeFile(
        directoryUri: Uri,
        fileName: String,
        mimeType: String,
        fileSize: Long = -1L,
        fileStream: InputStream,
        progressCallback: ((bytesRead: Long, totalBytes: Long) -> Unit)? = null
    ): Result<Unit>

    suspend fun getFileInfo(uri: Uri): Result<Map<String, String?>>

    suspend fun cacheFile(
        fileName: String,
        fileSize: Long = -1L,
        fileStream: InputStream,
        progressCallback: ((bytesRead: Long, totalBytes: Long) -> Unit)? = null
    ): Result<Uri>

}