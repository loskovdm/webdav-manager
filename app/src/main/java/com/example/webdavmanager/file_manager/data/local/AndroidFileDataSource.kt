package com.example.webdavmanager.file_manager.data.local

import android.content.Context
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import java.io.InputStream
import javax.inject.Inject

class AndroidFileDataSource @Inject constructor(private val context: Context) {
    fun createFile(directoryUri: Uri, fileName: String, mimeType: String): Result<Uri> = runCatching {
        val destinationDirectory = DocumentFile.fromTreeUri(context, directoryUri)
            ?: throw IllegalArgumentException("Invalid directory URI: $directoryUri")
        val newFile = destinationDirectory.createFile(mimeType, fileName)
            ?: throw IllegalStateException("Failed to create file in directory: $directoryUri")
        newFile.uri
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
}