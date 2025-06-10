package io.github.loskovdm.webdavmanager.feature.filemanager

import android.net.Uri
import io.github.loskovdm.webdavmanager.feature.filemanager.component.SortOrder
import io.github.loskovdm.webdavmanager.feature.filemanager.model.FileItem

data class FileManagerState(
    val serverId: Int = 0,

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

    val copiedFile: FileItem? = null,

    val isInitialLoadFileList: Boolean = false,
    val shouldNavigateBack: Boolean = false
)

enum class OperationType {
    UPLOAD, DOWNLOAD, OPEN, DELETE, COPY
}