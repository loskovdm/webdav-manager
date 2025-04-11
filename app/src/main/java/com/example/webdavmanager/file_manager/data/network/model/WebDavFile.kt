package com.example.webdavmanager.file_manager.data.network.model

import java.time.OffsetDateTime

data class WebDavFile(
    val name: String,
    val path: String,
    val isDirectory: Boolean,
    val size: Long?,
    val creationDate: OffsetDateTime?,
    val modifiedDate: OffsetDateTime?
)
