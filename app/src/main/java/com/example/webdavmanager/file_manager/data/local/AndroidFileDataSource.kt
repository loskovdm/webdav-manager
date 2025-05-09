package com.example.webdavmanager.file_manager.data.local

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.documentfile.provider.DocumentFile
import java.io.File
import java.io.InputStream
import javax.inject.Inject

class AndroidFileDataSource @Inject constructor(private val context: Context) {
    fun createFileWithSaf(directoryUri: Uri, fileName: String, mimeType: String): Result<Uri> = runCatching {
        Log.d("loadTest", "Run AndroidFileDataSource.createFileWithSaf")
        val destinationDirectory = DocumentFile.fromTreeUri(context, directoryUri)
            ?: throw IllegalArgumentException("Invalid directory URI: $directoryUri")
        val newFile = destinationDirectory.createFile(mimeType, fileName)
            ?: throw IllegalStateException("Failed to create file in directory: $directoryUri")
        Log.d("loadTest", "File uri: ${newFile.uri}")
        newFile.uri
    }

    fun createFileInDownloads(fileName: String, mimeType: String): Result<Uri> = runCatching {
        Log.d("loadTest", "Run AndroidFileDataSource.createFileInDownloads")
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
        Log.d("loadTest", "Run AndroidFileDataSource.writeFile")
        context.contentResolver.openOutputStream(uri)?.use { outputStream ->
            inputStream.copyTo(outputStream)
        } ?: throw IllegalArgumentException("Could not open output stream for URI: $uri")
    }
}