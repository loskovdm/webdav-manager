package com.example.webdavmanager.file_manager.ui.util

import android.webkit.MimeTypeMap
import com.example.webdavmanager.file_manager.ui.model.FileType

object FileTypeUtils {
    fun getFileType(url: String): FileType {
        if (url.endsWith('/')) return FileType.FOLDER

        val extension = MimeTypeMap.getFileExtensionFromUrl(url)
        val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)

        return when {
            mimeType == null -> FileType.UNKNOWN
            mimeType.startsWith("image/") -> FileType.IMAGE
            mimeType.startsWith("video/") -> FileType.VIDEO
            mimeType.startsWith("audio/") -> FileType.AUDIO
            mimeType == "application/pdf" -> FileType.PDF
            mimeType.contains("zip") || mimeType.contains("tar") || mimeType.contains("compress") -> FileType.ARCHIVE
            mimeType.startsWith("text/") || mimeType.contains("document") || mimeType.contains("sheet") -> FileType.DOCUMENT
            else -> FileType.UNKNOWN
        }
    }
}