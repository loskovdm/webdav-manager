package io.github.loskovdm.webdavmanager.core.storage.android

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.OpenableColumns
import androidx.core.content.FileProvider
import androidx.documentfile.provider.DocumentFile
import io.github.loskovdm.webdavmanager.core.storage.android.util.ProgressInputStream
import java.io.File
import java.io.InputStream
import javax.inject.Inject

class AndroidFileDataSource @Inject constructor(private val context: Context) {

    fun createFileWithSaf(directoryUri: Uri, fileName: String, mimeType: String): Result<Uri> = runCatching {
        val destinationDirectory = DocumentFile.fromTreeUri(context, directoryUri)
            ?: throw IllegalArgumentException("Invalid directory URI: $directoryUri")
        val newFile = destinationDirectory.createFile(mimeType, fileName)
            ?: throw IllegalStateException("Failed to create file in directory: $directoryUri")
        newFile.uri
    }

    fun createFileInDownloads(fileName: String, mimeType: String): Result<Uri> = runCatching {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val contentValues = ContentValues().apply {
                put(MediaStore.Downloads.DISPLAY_NAME, fileName)
                put(MediaStore.Downloads.MIME_TYPE, mimeType)
            }

            val uri = context.contentResolver.insert(
                MediaStore.Downloads.EXTERNAL_CONTENT_URI,
                contentValues
            ) ?: throw java.lang.IllegalArgumentException("Failed to create file in Downloads")

            uri
        } else {
            val downloadsDirectory = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS
            )
            if (!downloadsDirectory.exists()) {
                downloadsDirectory.mkdirs()
            }
            val file = File(downloadsDirectory, fileName)
            val uri = Uri.fromFile(file)
            uri
        }
    }

    fun readFile(uri: Uri): Result<InputStream> = runCatching {
        context.contentResolver.openInputStream(uri)
            ?: throw IllegalArgumentException("Could not open input stream for URI: $uri")
    }

    fun writeFile(uri: Uri, inputStream: InputStream): Result<Unit> = runCatching {
        context.contentResolver.openOutputStream(uri)?.use { outputStream ->
            inputStream.copyTo(outputStream)
        } ?: throw IllegalArgumentException("Could not open output stream for URI: $uri")
    }

    fun cacheFile(
        fileName: String,
        fileSize: Long,
        fileStream: InputStream,
        progressCallback: ((bytesRead: Long, totalBytes: Long) -> Unit)?
    ): Result<Uri>  {

        val totalBytes = if(fileSize > 0) {
            fileSize
        } else {
            try {
                fileStream.available().toLong().takeIf { it > 0 } ?: -1L
            } catch (_: Exception) {
                -1L
            }
        }

        val progressAwareStream = if (progressCallback != null && totalBytes > 0) {
            ProgressInputStream(fileStream, totalBytes, progressCallback)
        } else {
            fileStream
        }

        try {
            val cacheDir = context.cacheDir
            val file = File(cacheDir, fileName)

            file.outputStream().use { outputStream ->
                progressAwareStream.copyTo(outputStream)
            }
            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )

            return Result.success(uri)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    fun getFileInfo(uri: Uri): Result<Map<String, String?>> {
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

    fun isDownloadsUri(uri: Uri): Boolean {
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

    fun getFileSize(uri: Uri): Long {
        return try {
            context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                if (cursor.moveToFirst()) {
                    val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
                    if (sizeIndex != -1) cursor.getLong(sizeIndex) else -1L
                } else -1L
            } ?: -1L
        } catch (_: Exception) {
            -1L
        }
    }

}