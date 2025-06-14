package io.github.loskovdm.webdavmanager.core.data.repository

import io.github.loskovdm.webdavmanager.core.data.model.FileModel
import io.github.loskovdm.webdavmanager.core.data.model.NetworkErrorModel
import io.github.loskovdm.webdavmanager.core.data.model.ServerModel
import io.github.loskovdm.webdavmanager.core.data.model.asExternalModel
import io.github.loskovdm.webdavmanager.core.data.model.asNetworkErrorModel
import io.github.loskovdm.webdavmanager.core.data.model.asWebDavConnectionInfo
import io.github.loskovdm.webdavmanager.core.storage.webdav.WebDavFileDataSource
import io.github.loskovdm.webdavmanager.core.storage.webdav.model.WebDavError
import java.io.InputStream
import java.net.URLEncoder
import javax.inject.Inject

internal class WebDavFileRepositoryImpl @Inject constructor(
    private val dataSource: WebDavFileDataSource
): WebDavFileRepository {

    override suspend fun setServerConnectionInfo(
        server: ServerModel
    ): Result<Unit> {
        return dataSource.setServerConnectionInfo(server.asWebDavConnectionInfo())
    }

    override suspend fun getFileList(
        directoryUri: String
    ): Result<List<FileModel>> {
        return dataSource.getFileList(directoryUri).fold(
            onSuccess = { webDavFiles ->
                Result.success(webDavFiles.map { it.asExternalModel() })
            },
            onFailure = { error ->
                Result.failure(
                    when (error) {
                        is WebDavError -> error.asNetworkErrorModel()
                        else -> NetworkErrorModel.Unknown(error)
                    }
                )
            }
        )
    }

    override suspend fun uploadFile(
        fileStreamProvider: () -> InputStream,
        directoryUri: String,
        nameFile: String,
        mimeType: String
    ): Result<Unit> {
        return dataSource.uploadFile(fileStreamProvider, directoryUri + nameFile, mimeType).fold(
            onSuccess = { Result.success(it) },
            onFailure = { error ->
                Result.failure(
                    when (error) {
                        is WebDavError -> error.asNetworkErrorModel()
                        else -> NetworkErrorModel.Unknown(error)
                    }
                )
            }
        )
    }

    override suspend fun downloadFile(
        fileUri: String
    ): Result<InputStream> {
        return dataSource.downloadFile(fileUri).fold(
            onSuccess = { Result.success(it) },
            onFailure = { error ->
                Result.failure(
                    when (error) {
                        is WebDavError -> error.asNetworkErrorModel()
                        else -> NetworkErrorModel.Unknown(error)
                    }
                )
            }
        )
    }

    override suspend fun moveFile(
        fileUri: String,
        destinationDirectoryUri: String
    ): Result<Unit> {
        val destinationFileUri = destinationDirectoryUri + fileUri.substringAfterLast('/')
        return dataSource.moveFile(fileUri, destinationFileUri).fold(
            onSuccess = { Result.success(it) },
            onFailure = { error ->
                Result.failure(
                    when (error) {
                        is WebDavError -> error.asNetworkErrorModel()
                        else -> NetworkErrorModel.Unknown(error)
                    }
                )
            }
        )
    }

    override suspend fun copyFile(
        fileUri: String,
        destinationDirectoryUri: String
    ): Result<Unit> {
        val destinationFileUri = destinationDirectoryUri + fileUri.substringAfterLast('/')
        return dataSource.copyFile(fileUri, destinationFileUri).fold(
            onSuccess = { Result.success(it) },
            onFailure = { error ->
                Result.failure(
                    when (error) {
                        is WebDavError -> error.asNetworkErrorModel()
                        else -> NetworkErrorModel.Unknown(error)
                    }
                )
            }
        )
    }

    override suspend fun deleteFile(
        fileUri: String
    ): Result<Unit> {
        return dataSource.deleteFile(fileUri).fold(
            onSuccess = { Result.success(it) },
            onFailure = { error ->
                Result.failure(
                    when (error) {
                        is WebDavError -> error.asNetworkErrorModel()
                        else -> NetworkErrorModel.Unknown(error)
                    }
                )
            }
        )
    }

    override suspend fun createDirectory(
        destinationDirectoryUri: String,
        name: String
    ): Result<Unit> {
        val directoryUri = destinationDirectoryUri + name
        return dataSource.createDirectory(directoryUri).fold(
            onSuccess = { Result.success(it) },
            onFailure = { error ->
                Result.failure(
                    when (error) {
                        is WebDavError -> error.asNetworkErrorModel()
                        else -> NetworkErrorModel.Unknown(error)
                    }
                )
            }
        )
    }

    override suspend fun renameFile(
        fileUri: String,
        newName: String
    ): Result<Unit> {
        return try {
            val protocolAndHost = fileUri.substringBefore("://") + "://" +
                    fileUri.substringAfter("://").substringBefore("/")
            val rawPath = fileUri.substringAfter(protocolAndHost)

            val parentPath = rawPath.trimEnd('/').substringBeforeLast("/")

            fun encodeSegment(segment: String) =
                URLEncoder
                    .encode(segment, "UTF-8")
                    .replace("+", "%20")

            val encodedParent = parentPath
                .split("/")
                .joinToString("/") { encodeSegment(it) }
            val encodedNewName = encodeSegment(newName)

            val newUrl = buildString {
                append(protocolAndHost)
                append(encodedParent)
                append("/")
                append(encodedNewName)
            }

            dataSource.moveFile(fileUri, newUrl).fold(
                onSuccess = { Result.success(it) },
                onFailure = { error ->
                    Result.failure(
                        when (error) {
                            is WebDavError -> error.asNetworkErrorModel()
                            else -> NetworkErrorModel.Unknown(error)
                        }
                    )
                }
            )
        } catch (e: Exception) {
            Result.failure(NetworkErrorModel.Unknown(e))
        }
    }

}