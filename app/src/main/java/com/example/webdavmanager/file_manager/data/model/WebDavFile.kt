package com.example.webdavmanager.file_manager.data.model

import java.util.Date

data class WebDavFile(
    val name: String,
    val uri: String,
    val mimeType: String?,
    val isDirectory: Boolean,
    val size: Long?,
    val creationDate: Date?,
    val modifiedDate: Date?
)