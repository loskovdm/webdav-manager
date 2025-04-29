package com.example.webdavmanager.file_manager.ui.model

import java.util.Date

data class FileItem(
    val name: String,
    val path: String,
    val fileType: FileType,
    val size: Long?,
    val creationDate: Date?,
    val modifiedDate: Date?
)
