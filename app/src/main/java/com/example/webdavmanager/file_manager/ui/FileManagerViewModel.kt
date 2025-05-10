package com.example.webdavmanager.file_manager.ui

import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.compose.ui.state.ToggleableState
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.webdavmanager.core.data.repository.ServerRepository
import com.example.webdavmanager.file_manager.data.repository.FileManagerRepository
import com.example.webdavmanager.file_manager.ui.model.FileItem
import com.example.webdavmanager.file_manager.ui.model.ServerConnectionInfo
import com.example.webdavmanager.file_manager.ui.util.toFileItem
import com.example.webdavmanager.file_manager.ui.util.toServerConnectionInfo
import com.example.webdavmanager.file_manager.ui.util.toWebDavConnectionInfo
import com.example.webdavmanager.file_manager.ui.util.toWebDavFile
import com.example.webdavmanager.navigation.NavDestination.ServerConfigDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FileManagerViewModel @Inject constructor(
    private val fileManagerRepository: FileManagerRepository,
    private val serverRepository: ServerRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val serverId: Int = savedStateHandle
        .toRoute<ServerConfigDestination>()
        .serverId

    private val directoryStack = ArrayDeque<String>()

    private val _state = MutableStateFlow(FileManagerState())
    val state: StateFlow<FileManagerState> = _state

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val serverConnectionInfo = serverRepository.getServerById(serverId)
                ?.toServerConnectionInfo()
                ?: ServerConnectionInfo("", "", "")

            val rootUri = serverConnectionInfo.url
            directoryStack.clear()
            directoryStack.add(rootUri)

            fileManagerRepository.setServerConnectionInfo(serverConnectionInfo.toWebDavConnectionInfo())
                .onFailure { throwable ->
                    _state.update { it.copy(errorMessage = throwable.toString()) }
                    Log.d("loadTest", "Throwable in init: ${throwable.toString()}")
                }

            loadFileList(rootUri)
        }
    }

    fun loadFileList(directoryUri: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(isLoading = true) }
            Log.d("loadTest", "Run loadFileList")
            fileManagerRepository
                .getRemoteFileList(directoryUri)
                .fold(
                    onSuccess = { files ->
                        val fileList = files.map { it.toFileItem() }
                        _state.update { it.copy(fileList = fileList) }

                        val directoryName = directoryUri.let {
                            val trimmedPath = it.trimEnd('/')
                            trimmedPath.substringAfterLast('/')
                        }
                        _state.update { it.copy(currentDirectoryName = directoryName) }

                        Log.d("loadTest", "State: ${_state.value.fileList}")
                    },
                    onFailure = { throwable ->
                        _state.update {
                            it.copy(errorMessage = throwable.toString())
                        }
                        Log.d("loadTest", "Throwable in loadFileList: ${throwable.toString()}")
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
                file.toWebDavFile(),
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
                            errorMessage = throwable.toString(),
                            isOperationInProgress = false
                        )
                    }
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
                onFailure = { throwable ->
                    Log.d("loadTest", "Throwable in renameFile: ${throwable.toString()}")
                    _state.update {
                        it.copy(errorMessage = throwable.toString())
                    }
                }
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
                remoteFile = file.toWebDavFile(),
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
                            errorMessage = throwable.toString(),
                            isOperationInProgress = false
                        )
                    }
                    Log.d("loadTest", "Throwable in downloadFileToDownloads: ${throwable.toString()}")
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
                remoteFile = file.toWebDavFile(),
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
                            errorMessage = throwable.toString(),
                            isOperationInProgress = false
                        )
                    }
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
                                    errorMessage = throwable.toString(),
                                    isOperationInProgress = false
                                )
                            }
                        }
                    )
                },
                onFailure = { throwable ->
                    _state.update { it.copy(errorMessage = throwable.toString()) }
                }
            )
        }
    }

    fun renameFile(fileUri: String, newName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("loadTest", "Run renameFile")

            fileManagerRepository.renameFile(fileUri, newName).fold(
                onSuccess = { loadFileList(directoryStack.last()) },
                onFailure = { throwable ->
                    Log.d("loadTest", "Throwable in renameFile: ${throwable.toString()}")
                    _state.update {
                        it.copy(errorMessage = throwable.toString())
                    }
                }
            )
        }
    }

    fun deleteFile(fileUri: String) {
        viewModelScope.launch(Dispatchers.IO) {
            fileManagerRepository.deleteFile(fileUri).fold(
                onSuccess = { loadFileList(directoryStack.last()) },
                onFailure = { throwable ->
                    _state.update {
                        it.copy(errorMessage = throwable.toString())
                    }
                }
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
        _state.update { it.copy(errorMessage = null) }
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
                        onFailure = { throwable ->
                            _state.update {
                                it.copy(errorMessage = throwable.toString())
                            }
                        }
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

}