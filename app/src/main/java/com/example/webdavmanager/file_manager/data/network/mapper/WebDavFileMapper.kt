package com.example.webdavmanager.file_manager.data.network.mapper

import com.example.webdavmanager.file_manager.data.network.model.WebDavFile
import com.thegrizzlylabs.sardineandroid.DavResource

fun DavResource.toWebDavFile(): WebDavFile {
    return WebDavFile(
        name = this.name ?: "",
        path = this.path,
        isDirectory = this.isDirectory,
        size = this.contentLength,
        creationDate = this.creation,
        modifiedDate = this.modified
    )
}