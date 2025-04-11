package com.example.webdavmanager.file_manager.data.network.model

import java.util.Date

data class WebDavFile(
    val name: String,
    val path: String,
    val isDirectory: Boolean,
    val size: Long?,
    val creationDate: Date?,
    val modifiedDate: Date?
)
