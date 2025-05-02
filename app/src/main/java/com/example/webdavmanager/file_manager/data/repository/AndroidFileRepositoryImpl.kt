package com.example.webdavmanager.file_manager.data.repository

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import com.example.webdavmanager.file_manager.data.local.AndroidFileDataSource
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.InputStream
import javax.inject.Inject

class AndroidFileRepositoryImpl @Inject constructor(
    private val dataSource: AndroidFileDataSource,
    @ApplicationContext private val context: Context
) : AndroidFileRepository {
    override suspend fun readFile(uri: Uri): Result<InputStream> {
        return dataSource.readFile(uri)
    }

    override suspend fun writeFile(
        directoryUri: Uri,
        fileName: String,
        mimeType: String,
        fileStream: InputStream
    ): Result<Unit> {
        return dataSource.createFile(directoryUri, fileName, mimeType).fold(
            onSuccess = { fileUri ->
                dataSource.writeFile(fileUri, fileStream)
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
}