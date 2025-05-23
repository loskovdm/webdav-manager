package io.github.loskovdm.webdavmanager.core.data.repository

import android.content.Context
import android.net.Uri
import io.github.loskovdm.webdavmanager.core.storage.android.AndroidFileDataSource
import io.github.loskovdm.webdavmanager.core.storage.android.util.ProgressInputStream
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.InputStream
import javax.inject.Inject

class AndroidFileRepositoryImpl @Inject constructor(
    private val dataSource: AndroidFileDataSource,
    @ApplicationContext private val context: Context
) : AndroidFileRepository {

    override suspend fun readFile(
        uri: Uri,
        progressCallback: ((bytesRead: Long, totalBytes: Long) -> Unit)?
    ): Result<InputStream> {
        return try {
            val fileSize = getFileSize(uri)
            val originalStreamResult = dataSource.readFile(uri)

            originalStreamResult.map { originalStream ->
                if (progressCallback != null && fileSize > 0) {
                    ProgressInputStream(originalStream, fileSize, progressCallback)
                } else {
                    originalStream
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun writeFile(
        directoryUri: Uri,
        fileName: String,
        mimeType: String,
        fileSize: Long,
        fileStream: InputStream,
        progressCallback: ((bytesRead: Long, totalBytes: Long) -> Unit)?
    ): Result<Unit> {

        val totalBytes = if(fileSize > 0) {
            fileSize
        } else {
            try {
                fileStream.available().toLong().takeIf { it > 0 } ?: -1L
            } catch (e: Exception) {
                -1L
            }
        }

        val progressAwareStream = if (progressCallback != null && totalBytes > 0) {
            ProgressInputStream(fileStream, totalBytes, progressCallback)
        } else {
            fileStream
        }

        val fileUriResult = if (isDownloadsUri(directoryUri)) {
            dataSource.createFileInDownloads(fileName, mimeType)
        } else {
            dataSource.createFileWithSaf(directoryUri, fileName, mimeType)
        }

        return fileUriResult.fold(
            onSuccess = { uri ->
                dataSource.writeFile(uri, progressAwareStream)
            },
            onFailure = { throwable ->
                Result.failure(throwable)
            }
        )
    }

    override suspend fun getFileInfo(uri: Uri): Result<Map<String, String?>> {
        return dataSource.getFileInfo(uri)
    }

    override suspend fun cacheFile(
        fileName: String,
        fileSize: Long,
        fileStream: InputStream,
        progressCallback: ((bytesRead: Long, totalBytes: Long) -> Unit)?
    ): Result<Uri> {
        return dataSource.cacheFile(
            fileName = fileName,
            fileSize = fileSize,
            fileStream = fileStream,
            progressCallback = progressCallback,
        )
    }

    private fun isDownloadsUri(uri: Uri): Boolean {
        return dataSource.isDownloadsUri(uri)
    }

    private fun getFileSize(uri: Uri): Long {
        return dataSource.getFileSize(uri)
    }

}