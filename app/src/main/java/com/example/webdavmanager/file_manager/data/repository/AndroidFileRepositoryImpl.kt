package com.example.webdavmanager.file_manager.data.repository

import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import androidx.core.content.FileProvider
import com.example.webdavmanager.file_manager.data.local.AndroidFileDataSource
import com.example.webdavmanager.file_manager.data.util.ProgressInputStream
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
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
        Log.d("loadTest", "Run AndroidFileRepositoryImpl.writeFile")
        Log.d("loadTest", "writeFile with size: $fileSize and callback: ${progressCallback != null}")

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
            Log.d("loadTest", "Creating progress stream with size: $fileSize")
            ProgressInputStream(fileStream, totalBytes, progressCallback)
        } else {
            Log.d("loadTest", "Using regular stream, no progress tracking")
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
        return try {
            val fileInfo = mutableMapOf<String, String?>()

            context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                if (cursor.moveToFirst()) {
                    val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    if (nameIndex != -1) {
                        fileInfo["name"] = cursor.getString(nameIndex)
                    }
                }
            }

            fileInfo["mimeType"] = context.contentResolver.getType(uri)

            Result.success(fileInfo)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun cacheFile(
        fileName: String,
        mimeType: String,
        fileSize: Long,
        fileStream: InputStream,
        progressCallback: ((bytesRead: Long, totalBytes: Long) -> Unit)?
    ): Result<Uri>  {
        Log.d("loadTest", "Run cacheFile")

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
            Log.d("loadTest", "Creating progress stream with size: $fileSize")
            ProgressInputStream(fileStream, totalBytes, progressCallback)
        } else {
            Log.d("loadTest", "Using regular stream, no progress tracking")
            fileStream
        }

        try {
            val cacheDir = context.cacheDir
            val file = File(cacheDir, fileName)

            file.outputStream().use { outputStream ->
                progressAwareStream.copyTo(outputStream)
            }
            Log.d("loadTest", "Run cacheFile - before getUriForFile")
            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )
            Log.d("loadTest", "Run cacheFile - after getUriForFile: $uri")

            return Result.success(uri)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    override suspend fun clearCache(): Result<Unit> {
        Log.d("loadTest", "Run clearCache")
        try {
            context.cacheDir.listFiles()?.forEach { it.delete() }
            Log.d("loadTest", "Run clearCache")
            return Result.success(Unit)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    private fun isDownloadsUri(uri: Uri): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (uri == MediaStore.Downloads.EXTERNAL_CONTENT_URI) return true
        }

        if (uri.scheme == "content" && uri.authority == "com.android.externalstorage.documents"
        ) {
            val docId = DocumentsContract.getTreeDocumentId(uri)
            val folder = docId.substringAfter(":", "")
            if (folder == Environment.DIRECTORY_DOWNLOADS) return true
        }
        return false
    }

    private fun getFileSize(uri: Uri): Long {
        return try {
            context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                if (cursor.moveToFirst()) {
                    val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
                    if (sizeIndex != -1) cursor.getLong(sizeIndex) else -1L
                } else -1L
            } ?: -1L
        } catch (e: Exception) {
            Log.e("loadTest", "Error getting file size", e)
            -1L
        }
    }
}