package io.github.loskovdm.webdavmanager.file_manager.data.mapper

import io.github.loskovdm.webdavmanager.file_manager.data.model.WebDavFile
import com.thegrizzlylabs.sardineandroid.DavResource

fun DavResource.toWebDavFile(baseServerUri: String): WebDavFile {
    val normalizedContentType = when {
        this.isDirectory -> null
        this.contentType.isNotBlank() -> this.contentType
        else -> "application/octet-stream"
    }

    val trimFileUri = this.path.trim('/').substringAfter('/')
    var absoluteFileUri = baseServerUri + trimFileUri

    if (this.isDirectory && !absoluteFileUri.endsWith('/')) absoluteFileUri = "$absoluteFileUri/"

    return WebDavFile(
        name = this.name ?: "",
        uri = absoluteFileUri,
        mimeType = normalizedContentType,
        isDirectory = this.isDirectory,
        size = this.contentLength,
        creationDate = this.creation,
        modifiedDate = this.modified
    )
}