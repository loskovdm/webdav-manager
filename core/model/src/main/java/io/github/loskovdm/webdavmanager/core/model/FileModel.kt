package io.github.loskovdm.webdavmanager.core.model

import java.util.Date

data class FileModel(
    val name: String,
    val uri: String,
    val mimeType: String?,
    val isDirectory: Boolean,
    val size: Long?,
    val creationDate: Date?,
    val modifiedDate: Date?
)