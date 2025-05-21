package io.github.loskovdm.webdavmanager.file_manager.ui.model

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
