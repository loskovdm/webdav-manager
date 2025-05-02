package com.example.webdavmanager.file_manager.data.repository

import android.net.Uri
import java.io.InputStream

interface AndroidFileRepository {
    suspend fun readFile(uri: Uri): Result<InputStream>
    suspend fun writeFile(directoryUri: Uri, fileName: String, mimeType: String, fileStream: InputStream): Result<Unit>
    suspend fun getFileInfo(uri: Uri): Result<Map<String, String?>>
}