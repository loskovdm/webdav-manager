package io.github.loskovdm.webdavmanager.core.data.model

import io.github.loskovdm.webdavmanager.core.storage.webdav.model.WebDavFile
import java.util.Date

data class File(
    val name: String,
    val uri: String,
    val mimeType: String?,
    val isDirectory: Boolean,
    val size: Long?,
    val creationDate: Date?,
    val modifiedDate: Date?
)

fun WebDavFile.asExternalModel() =
    File(
        name = name,
        uri = uri,
        mimeType = mimeType,
        isDirectory = isDirectory,
        size = size,
        creationDate = creationDate,
        modifiedDate = modifiedDate
    )
