package com.example.webdavmanager.file_manager.ui

import android.net.Uri
import com.example.webdavmanager.file_manager.ui.component.SortOrder
import com.example.webdavmanager.file_manager.ui.model.FileItem

data class FileManagerState(
    val currentDirectoryName: String = "",
    val fileList: List<FileItem> = emptyList<FileItem>(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val fileToCustomDownload: FileItem? = null,
    val cachedFileInfo: Pair<Uri, String?>? = null,
    val fileInfoToShow: FileItem? = null,

    val isOperationInProgress: Boolean = false,
    val operationType: OperationType? = null,
    val operationProgress: Float = 0f,
    val operationFileName: String? = null,

    val isSelectionModeActive: Boolean = false,
    val selectedFiles: Set<String> = emptySet(),

    val isShowSearchBar: Boolean = false,
    val searchQuery: String = "",

    val sortOrder: SortOrder = SortOrder.NAME_ASC,

    val copiedFile: FileItem? = null
)

enum class OperationType {
    UPLOAD, DOWNLOAD, OPEN, DELETE, COPY
}