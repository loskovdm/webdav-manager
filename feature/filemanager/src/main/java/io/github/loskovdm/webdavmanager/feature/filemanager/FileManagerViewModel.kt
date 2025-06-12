package io.github.loskovdm.webdavmanager.feature.filemanager

import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.loskovdm.webdavmanager.core.data.repository.ServerRepository
import io.github.loskovdm.webdavmanager.core.data.repository.FileManagerRepository
import io.github.loskovdm.webdavmanager.feature.filemanager.component.SortOrder
import io.github.loskovdm.webdavmanager.feature.filemanager.model.FileItem
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.loskovdm.webdavmanager.core.data.model.NetworkErrorModel
import io.github.loskovdm.webdavmanager.core.data.model.ServerModel
import io.github.loskovdm.webdavmanager.feature.filemanager.model.asExternalModel
import io.github.loskovdm.webdavmanager.feature.filemanager.model.asFileItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FileManagerViewModel @Inject constructor(
    private val fileManagerRepository: FileManagerRepository,
    private val serverRepository: ServerRepository
): ViewModel() {

    fun setServerId(serverId: Int) {
        _state.update { it.copy(serverId = serverId) }

        viewModelScope.launch(Dispatchers.IO) {
            val server = serverRepository.getServerById(_state.value.serverId)
                ?: ServerModel(0, "", "", "", "")
            val serverConnectionInfo = server

            val rootUri = serverConnectionInfo.url
            directoryStack.clear()
            directoryStack.add(rootUri)

            fileManagerRepository.setServerConnectionInfo(server)
                .onFailure { throwable -> handleError(throwable) }

            loadFileList(rootUri, isInitialLoad = true)
        }
    }

    private val directoryStack = ArrayDeque<String>()

    private val _state = MutableStateFlow(FileManagerState())
    val state: StateFlow<FileManagerState> = _state

    fun loadFileList(directoryUri: String, isInitialLoad: Boolean? = null) {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update {
                it.copy(
                    isLoading = true,
                    isInitialLoadFileList = isInitialLoad == true
                )
            }
            fileManagerRepository
                .getRemoteFileList(directoryUri)
                .fold(
                    onSuccess = { files ->
                        val fileList = files.map { it.asFileItem() }
                        _state.update { it.copy(fileList = sortFileList(fileList)) }

                        val directoryName = directoryUri.let {
                            val trimmedPath = it.trimEnd('/')
                            trimmedPath.substringAfterLast('/')
                        }
                        _state.update {
                            it.copy(
                                currentDirectoryName = directoryName,
                                isInitialLoadFileList = false
                            )
                        }

                        Log.d("loadTest", "State: ${_state.value.fileList}")
                    },
                    onFailure = {
                        throwable -> handleError(throwable)
                        directoryStack.removeLast()
                    }
                )
            _state.update { it.copy(isLoading = false) }
        }
    }

    fun canNavigateUp(): Boolean {
        return directoryStack.size > 1
    }

    fun navigateUp() {
        if (canNavigateUp()) {
            directoryStack.removeLast()
            val previousDirectory = directoryStack.last()
            loadFileList(previousDirectory)
        }
    }

    fun openFile(file: FileItem) {
        Log.d("loadTest", _state.toString())

        if (_state.value.isSelectionModeActive) {
            setSelectionModeActive()
        }
        else if (_state.value.isShowSearchBar) {
            setIsSearchBar()
            setSearchQuery("")
        }

        if (file.isDirectory) {
            val newDirectory = file.uri
            directoryStack.add(newDirectory)
            loadFileList(newDirectory)
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(
                isOperationInProgress = true,
                operationType = OperationType.OPEN,
                operationProgress = 0f,
                operationFileName = file.name
            )}

            fileManagerRepository.cacheFile(
                remoteFile = file.asExternalModel(),
                progressCallback = { bytesRead, totalBytes ->
                    val progressPercentage = if (totalBytes > 0) {
                        (bytesRead.toFloat() / totalBytes.toFloat()) * 100f
                    } else {
                        0f
                    }
                    _state.update { it.copy(operationProgress = progressPercentage) }
                    Log.d("loadTest", "Cache progress: $progressPercentage% ($bytesRead/$totalBytes)")
                }
            ).fold(
                onSuccess = { cachedUri ->
                    _state.update {
                        it.copy(
                            cachedFileInfo = Pair(cachedUri, file.mimeType),
                            isOperationInProgress = false
                        )
                    }
                },
                onFailure = { throwable ->
                    _state.update {
                        it.copy(
                            isOperationInProgress = false
                        )
                    }
                    handleError(throwable)
                    Log.d("loadTest", "Throwable in openFile (file): ${throwable.toString()}")
                }
            )
        }
        Log.d("loadTest", _state.toString())
    }

    fun createDirectory(destinationDirectoryUri: String = directoryStack.last(), name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            fileManagerRepository.createDirectory(
                remoteDestinationDirectoryUri = destinationDirectoryUri,
                name = name
            ).fold(
                onSuccess = { loadFileList(directoryStack.last()) },
                onFailure = { throwable -> handleError(throwable)}
            )
        }
    }

    fun downloadFileToDownloads(file: FileItem) {
        viewModelScope.launch(Dispatchers.IO) {
            val downloadsUri: Uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                MediaStore.Downloads.EXTERNAL_CONTENT_URI
            } else {
                val file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                Uri.fromFile(file)
            }

            _state.update { it.copy(
                isOperationInProgress = true,
                operationType = OperationType.DOWNLOAD,
                operationProgress = 0f,
                operationFileName = file.name
            )}

            fileManagerRepository.downloadFile(
                remoteFile = file.asExternalModel(),
                localDirectoryUri = downloadsUri,
                progressCallback = { bytesDownloaded, totalBytes ->
                    val progressPercentage = if (totalBytes > 0) {
                        (bytesDownloaded.toFloat() / totalBytes.toFloat()) * 100f
                    } else {
                        0f
                    }
                    _state.update { it.copy(operationProgress = progressPercentage) }
                    Log.d("loadTest", "Download progress: $progressPercentage% ($bytesDownloaded/$totalBytes)")
                    Log.e("loadTest", "Download progress in state: ${_state.value.operationProgress}")
                }
            ).fold(
                onSuccess = {
                    _state.update { it.copy(isOperationInProgress = false) }
                },
                onFailure = { throwable ->
                    _state.update {
                        it.copy(
                            isOperationInProgress = false
                        )
                    }
                    handleError(throwable)
                }
            )
        }
    }

    fun setFileToCustomDownloads(file: FileItem) {
        _state.update { it.copy(fileToCustomDownload = file) }
    }

    fun downloadsFileToCustomDirectory(file: FileItem, selectedDirectoryUri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(
                isOperationInProgress = true,
                operationType = OperationType.DOWNLOAD,
                operationProgress = 0f,
                operationFileName = file.name
            )}

            fileManagerRepository.downloadFile(
                remoteFile = file.asExternalModel(),
                localDirectoryUri = selectedDirectoryUri,
                progressCallback = { bytesDownloaded, totalBytes ->
                    val progressPercentage = if (totalBytes > 0) {
                        (bytesDownloaded.toFloat() / totalBytes.toFloat()) * 100f
                    } else {
                        0f
                    }
                    _state.update { it.copy(operationProgress = progressPercentage) }
                    Log.d("loadTest", "Download progress: $progressPercentage% ($bytesDownloaded/$totalBytes)")
                }
            ).fold(
                onSuccess = {
                    _state.update { it.copy(isOperationInProgress = false) }
                },
                onFailure = { throwable ->
                    _state.update {
                        it.copy(
                            isOperationInProgress = false
                        )
                    }
                    handleError(throwable)
                }
            )
        }
    }

    fun uploadFile(localFile: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            val currentDirectory = directoryStack.last()

            fileManagerRepository.getLocalFileInfo(localFile).fold(
                onSuccess = { fileInfo ->
                    val fileName = fileInfo["name"] ?: "Unknown file"

                    _state.update { it.copy(
                        isOperationInProgress = true,
                        operationType = OperationType.UPLOAD,
                        operationProgress = 0f,
                        operationFileName = fileName
                    )}

                    fileManagerRepository.uploadFile(
                        currentDirectory,
                        localFile,
                        progressCallback = { bytesUploaded, totalBytes ->
                            val progressPercentage = if (totalBytes > 0) {
                                (bytesUploaded.toFloat() / totalBytes.toFloat()) * 100f
                            } else {
                                0f
                            }
                            _state.update { it.copy(operationProgress = progressPercentage) }
                            Log.d("loadTest", "Progress: $progressPercentage% ($bytesUploaded/$totalBytes)")
                        }
                    ).fold(
                        onSuccess = {
                            Log.d("loadTest", "SUCCESS: Upload completed")
                            _state.update { it.copy(isOperationInProgress = false) }
                            loadFileList(currentDirectory)
                        },
                        onFailure = { throwable ->
                            _state.update {
                                it.copy(
                                    isOperationInProgress = false
                                )
                            }
                            handleError(throwable)
                        }
                    )
                },
                onFailure = { throwable -> handleError(throwable) }
            )
        }
    }

    fun renameFile(fileUri: String, newName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("loadTest", "Run renameFile")

            fileManagerRepository.renameFile(fileUri, newName).fold(
                onSuccess = { loadFileList(directoryStack.last()) },
                onFailure = { throwable -> handleError(throwable) }
            )
        }
    }

    fun deleteFile(fileUri: String) {
        viewModelScope.launch(Dispatchers.IO) {
            fileManagerRepository.deleteFile(fileUri).fold(
                onSuccess = { loadFileList(directoryStack.last()) },
                onFailure = { throwable -> handleError(throwable) }
            )
            Log.d("loadTest", directoryStack.toString())
        }
    }

    fun setErrorMessage(message: String) {
        _state.update { it.copy(errorMessage = message) }
    }

    fun clearCacheFileInfo() {
        _state.update { it.copy(cachedFileInfo = null) }
    }

    fun clearErrorMessage() {
        val wasInitialLoadError = _state.value.isInitialLoadFileList
        _state.update {
            it.copy(
                errorMessage = null,
                isInitialLoadFileList = false,
                shouldNavigateBack = wasInitialLoadError
            )
        }
    }

    fun cancelCurrentOperation() {
        _state.update { it.copy(isOperationInProgress = false) }
        loadFileList(directoryStack.last())
        Log.d("loadTest", "Upload operation canceled by user")
    }

    fun selectFile(fileUri: String) {
        Log.d("loadTest", "Run selectFile")
        _state.update { currentState ->
            val updatedSelectedFile = currentState.selectedFiles.toMutableSet()
            if (updatedSelectedFile.contains(fileUri)) {
                updatedSelectedFile.remove(fileUri)
            } else {
                updatedSelectedFile.add(fileUri)
            }
            currentState.copy(selectedFiles = updatedSelectedFile)
        }
    }

    fun clearSelection() {
        _state.update { currentState ->
            currentState.copy(selectedFiles = emptySet())
        }
    }

    fun getSelectionModeActive(): Boolean = _state.value.isSelectionModeActive

    fun setSelectionModeActive() {
        val currentSelectionModeActive = _state.value.isSelectionModeActive
        _state.update { it.copy(isSelectionModeActive = !currentSelectionModeActive) }
    }

    fun getNumberSelectedFiles(): Int {
        return _state.value.selectedFiles.count()
    }

    fun getParentCheckBoxIsSelected(): Boolean {
        return _state.value.selectedFiles.size == _state.value.fileList.size
    }

    fun parentCheckBoxSelect() {
        val currentParentCheckBoxState = getParentCheckBoxIsSelected()
        if (currentParentCheckBoxState) {
            _state.update { it.copy(selectedFiles = emptySet()) }
        } else {
            _state.update { it.copy(selectedFiles = it.fileList.map { file -> file.uri }.toSet()) }
        }
    }

    fun deleteCheckedFile() {
        viewModelScope.launch(Dispatchers.IO) {
            val selectedFiles = _state.value.selectedFiles

            if (selectedFiles.isNotEmpty()) {
                _state.update {
                    it.copy(
                        isOperationInProgress = true,
                        operationType = OperationType.DELETE,
                        operationProgress = 0f,
                        operationFileName = "${selectedFiles.size} files"
                    )
                }

                var deletedCount = 0

                selectedFiles.forEach { fileUri ->
                    fileManagerRepository.deleteFile(fileUri).fold(
                        onSuccess = {
                            deletedCount++
                            val process = (deletedCount.toFloat() / selectedFiles.size.toFloat())
                            _state.update { it.copy(operationProgress = process) }
                        },
                        onFailure = { throwable -> handleError(throwable) }
                    )
                }


                _state.update {
                    it.copy(
                        selectedFiles = emptySet(),
                        isSelectionModeActive = false,
                        isOperationInProgress = false
                    )
                }
            }

            loadFileList(directoryStack.last())
        }
    }

    fun showFileInfo(file: FileItem) {
        _state.update { it.copy(fileInfoToShow = file) }
    }

    fun hideFileInfo() {
        _state.update { it.copy(fileInfoToShow = null) }
    }

    fun setIsSearchBar() {
        val currentSearchBarState = _state.value.isShowSearchBar
        _state.update { it.copy(isShowSearchBar = !currentSearchBarState) }
    }

    fun setSearchQuery(query: String) {
        _state.update { it.copy(searchQuery = query) }
    }

    fun setSortOrder(order: SortOrder) {
        _state.update { it.copy(sortOrder = order) }
        loadFileList(directoryStack.last())
    }

    fun sortFileList(files: List<FileItem>): List<FileItem> {
        return when (_state.value.sortOrder) {
            SortOrder.NAME_ASC -> files.sortedBy { it.name.lowercase() }
            SortOrder.NAME_DESC -> files.sortedByDescending { it.name.lowercase() }
            SortOrder.SIZE_ASC -> files.sortedBy { it.size }
            SortOrder.SIZE_DESC -> files.sortedByDescending { it.size }
            SortOrder.DATE_ASC -> files.sortedBy { it.modifiedDate }
            SortOrder.DATE_DESC -> files.sortedByDescending { it.modifiedDate }
        }
    }

    fun copyFile(file: FileItem) {
        _state.update { it.copy(copiedFile = file) }
    }

    fun pasteFile() {
        viewModelScope.launch(Dispatchers.IO) {
            val file = _state.value.copiedFile ?: return@launch
            val currentDirectoryUri = directoryStack.last()

            val fileName = file.name

            val fileAlreadyExists = _state.value.fileList.any { it.name == fileName }

            if (fileAlreadyExists) {
                _state.update {
                    it.copy(
                        errorMessage = "A file with name '$fileName' already exists in this directory",
                        copiedFile = null
                    )
                }
                return@launch
            }

            fileManagerRepository.copyFile(file.uri, currentDirectoryUri).fold(
                onSuccess = {
                    loadFileList(currentDirectoryUri)
                    _state.update { it.copy(copiedFile = null) }
                },
                onFailure = { throwable -> handleError(throwable) }
            )
        }
    }

    private fun handleError(throwable: Throwable) {
        val errorMessage = when (throwable) {
            is NetworkErrorModel -> {
                // Handle specific network error types
                when (throwable) {
                    NetworkErrorModel.AccessDenied -> "Access denied. You don't have permission to perform this operation."
                    NetworkErrorModel.AuthenticationFailed -> "Authentication failed. Please check your authorization data."
                    NetworkErrorModel.ConnectionFailed -> "Connection failed. Check connection settings."
                    NetworkErrorModel.InvalidUrl -> "Invalid URL. Please check the server address."
                    NetworkErrorModel.MissingScheme -> "Invalid URL format. Missing protocol (http:// or https://)."
                    NetworkErrorModel.OperationNotSupported -> "This operation is not supported by the server."
                    NetworkErrorModel.ResourceNotFound -> "The requested file or folder was not found on the server."
                    NetworkErrorModel.SecurityError -> "Security error occurred. Check your internet connection and connection settings."
                    NetworkErrorModel.ServerError -> "Server error occurred. Please check your internet connection and try again."
                    NetworkErrorModel.Timeout -> "Connection timed out. The server took too long to respond. Please check your internet connection and try again."
                    is NetworkErrorModel.Unknown -> throwable.message ?: "An unknown error occurred."
                }
            }
            else -> {
                throwable.toString()
            }
        }

        _state.update { it.copy(errorMessage = errorMessage) }
    }
}