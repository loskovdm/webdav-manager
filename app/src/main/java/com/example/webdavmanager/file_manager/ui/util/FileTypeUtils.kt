package com.example.webdavmanager.file_manager.ui.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.InsertDriveFile
import androidx.compose.material.icons.filled.AudioFile
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.FolderZip
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Slideshow
import androidx.compose.material.icons.filled.VideoFile
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.webdavmanager.file_manager.ui.model.FileItem

fun getFileIcon(file: FileItem): ImageVector {
    if (file.isDirectory) return Icons.Default.Folder

    val mimeType = file.mimeType?.lowercase() ?: ""

    return when {
        mimeType.startsWith("image/") -> Icons.Default.Image
        mimeType.startsWith("video/") -> Icons.Default.VideoFile
        mimeType.startsWith("audio/") -> Icons.Default.AudioFile
        mimeType.startsWith("application/pdf") -> Icons.Default.PictureAsPdf
        mimeType.startsWith("text/plain") -> Icons.Default.Description
        mimeType.startsWith("application/zip") ||
                mimeType.startsWith("application/x-rar") -> Icons.Default.FolderZip
        mimeType.contains("msword") ||
                mimeType.contains("document") -> Icons.Default.Description
        mimeType.contains("presentation") -> Icons.Default.Slideshow

        else -> getIconByExtension(file.name)
    }
}

private fun getIconByExtension(fileName: String): ImageVector {
    val extension = fileName.substringAfterLast('.', "").lowercase()

    return when (extension) {
        // Images
        "jpg", "jpeg", "png", "gif", "bmp", "webp" -> Icons.Default.Image

        // Videos
        "mp4", "avi", "mkv", "mov", "webm" -> Icons.Default.VideoFile

        // Audio
        "mp3", "wav", "ogg", "flac", "aac" -> Icons.Default.AudioFile

        // Documents
        "pdf" -> Icons.Default.PictureAsPdf
        "doc", "docx", "odt", "txt", "rtf" -> Icons.Default.Description
        "ppt", "pptx", "odp" -> Icons.Default.Slideshow

        // Archives
        "zip", "rar", "tar", "gz", "7z" -> Icons.Default.FolderZip

        // Code
        "java", "kt", "xml", "json", "html", "css", "js", "py", "c", "cpp", "h" -> Icons.Default.Code

        // Default
        else -> Icons.AutoMirrored.Default.InsertDriveFile
    }
}