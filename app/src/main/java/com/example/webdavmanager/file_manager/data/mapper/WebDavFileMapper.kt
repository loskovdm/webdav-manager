package com.example.webdavmanager.file_manager.data.mapper

import com.example.webdavmanager.file_manager.data.model.WebDavFile
import com.thegrizzlylabs.sardineandroid.DavResource

fun DavResource.toWebDavFile(): WebDavFile {
    val normalizedContentType = when {
        this.isDirectory -> null
        this.contentType.isNotBlank() -> this.contentType
        else -> "application/octet-stream"
    }

    return WebDavFile(
        name = this.name ?: "",
        uri = this.path,
        mimeType = normalizedContentType,
        isDirectory = this.isDirectory,
        size = this.contentLength,
        creationDate = this.creation,
        modifiedDate = this.modified
    )
}