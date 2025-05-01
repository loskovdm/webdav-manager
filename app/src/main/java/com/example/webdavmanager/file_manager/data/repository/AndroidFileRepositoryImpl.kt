package com.example.webdavmanager.file_manager.data.repository

import android.net.Uri
import com.example.webdavmanager.file_manager.data.local.AndroidFileDataSource
import java.io.InputStream
import javax.inject.Inject

class AndroidFileRepositoryImpl @Inject constructor(
    val dataSource: AndroidFileDataSource
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
}