package io.github.loskovdm.webdavmanager.feature.filemanager.model

import io.github.loskovdm.webdavmanager.core.data.model.File
import java.util.Date

data class FileItem(
    val name: String,
    val uri: String,
    val mimeType: String?,
    val isDirectory: Boolean,
    val size: Long?,
    val creationDate: Date?,
    val modifiedDate: Date?
)

fun File.asFileItem() = FileItem(
    name = name,
    uri = uri,
    mimeType = mimeType,
    isDirectory = isDirectory,
    size = size,
    creationDate = creationDate,
    modifiedDate = modifiedDate
)

fun FileItem.asExternalModel() = File(
    name = name,
    uri = uri,
    mimeType = mimeType,
    isDirectory = isDirectory,
    size = size,
    creationDate = creationDate,
    modifiedDate = modifiedDate
)
