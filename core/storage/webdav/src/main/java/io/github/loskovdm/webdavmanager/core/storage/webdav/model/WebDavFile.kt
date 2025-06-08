package io.github.loskovdm.webdavmanager.core.storage.webdav.model

import com.thegrizzlylabs.sardineandroid.DavResource
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

fun DavResource.asWebDavFile(baseServerUri: String): WebDavFile {
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
