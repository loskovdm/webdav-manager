package io.github.loskovdm.webdavmanager.file_manager.ui.util

import io.github.loskovdm.webdavmanager.file_manager.data.model.WebDavFile
import io.github.loskovdm.webdavmanager.file_manager.ui.model.FileItem

fun WebDavFile.toFileItem(): FileItem {
    return FileItem(
        name = name,
        uri = uri,
        mimeType = mimeType,
        isDirectory = isDirectory,
        size = size,
        creationDate = creationDate,
        modifiedDate = modifiedDate
    )
}

fun FileItem.toWebDavFile(): WebDavFile {
    return WebDavFile(
        name = name,
        uri = uri,
        mimeType = mimeType,
        isDirectory = isDirectory,
        size = size,
        creationDate = creationDate,
        modifiedDate = modifiedDate
    )
}