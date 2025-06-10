package io.github.loskovdm.webdavmanager.feature.filemanager

import android.content.Intent
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.loskovdm.webdavmanager.core.ui.component.ErrorDialog
import io.github.loskovdm.webdavmanager.core.ui.component.LoadingContent
import io.github.loskovdm.webdavmanager.feature.filemanager.component.FileInfoDialog
import io.github.loskovdm.webdavmanager.feature.filemanager.component.FileList
import io.github.loskovdm.webdavmanager.feature.filemanager.component.FileManagerTopAppBar
import io.github.loskovdm.webdavmanager.feature.filemanager.component.FileNameInputDialog
import io.github.loskovdm.webdavmanager.feature.filemanager.component.ProgressDialog
import io.github.loskovdm.webdavmanager.feature.filemanager.model.FileItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FileManagerScreen(
    viewModel: FileManagerViewModel = hiltViewModel(),
    serverId: Int,
    onNavigateBack: () -> Unit
) {
    LaunchedEffect(serverId) {
        viewModel.setServerId(serverId)
    }

    val context = LocalContext.current

    val state by viewModel.state.collectAsStateWithLifecycle()

    var dialogState by remember { mutableStateOf<DialogState>(DialogState.None) }

    val directoryPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocumentTree()
    ) { uri ->
        uri?.let { selectedDirectory ->
            state.fileToCustomDownload?.let { file ->
                viewModel.downloadsFileToCustomDirectory(file, selectedDirectory)
            }
        }
    }

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        uri?.let { selectedFileUri ->
            viewModel.uploadFile(selectedFileUri)
        }
    }

    BackHandler {
        if (state.isSelectionModeActive) {
            viewModel.setSelectionModeActive()
            viewModel.clearSelection()
        }
        else if (state.isShowSearchBar) {
            viewModel.setIsSearchBar()
            viewModel.setSearchQuery("")
        }
        else if (viewModel.canNavigateUp()) {
            viewModel.navigateUp()
        } else {
            onNavigateBack()
        }
    }

    val displayedFiles = if (state.searchQuery.isNotEmpty()) {
        state.fileList.filter { file ->
            file.name.contains(state.searchQuery, ignoreCase = true) ||
                    isSubsequence(state.searchQuery.lowercase(), file.name.lowercase()) ||
                    levenshteinDistance(state.searchQuery.lowercase(), file.name.lowercase()) <=
                    maxOf(2, state.searchQuery.length / 3)
        }
    } else {
        state.fileList
    }

    Scaffold(
        topBar = {
            FileManagerTopAppBar(
                directoryName = state.currentDirectoryName,
                onNavigateBack = {
                    if (viewModel.canNavigateUp()) {
                        viewModel.navigateUp()
                    } else {
                        onNavigateBack()
                    }
                },
                onUploadFileClick = { filePickerLauncher.launch(arrayOf("*/*")) },
                onCreateFolderClick = { dialogState = DialogState.CreateFolder },
                isSelectionModeActive = viewModel.getSelectionModeActive(),
                numberSelectedFiles = viewModel.getNumberSelectedFiles(),
                parentCheckBoxIsSelected = viewModel.getParentCheckBoxIsSelected(),
                onParentCheckBoxSelect = { viewModel.parentCheckBoxSelect() },
                closeSelectionModeActive = {
                    viewModel.setSelectionModeActive()
                    viewModel.clearSelection()
                },
                onDeleteCheckedFile = { viewModel.deleteCheckedFile() },
                isShowSearchBar = state.isShowSearchBar,
                onSearchClick = { viewModel.setIsSearchBar() },
                onCloseSearch = {
                    viewModel.setIsSearchBar()
                    viewModel.setSearchQuery("")
                },
                currentSearchQuery = state.searchQuery,
                onSearchQueryChange = viewModel::setSearchQuery,
                currentSortOrder = state.sortOrder,
                onSortOptionSelect = { order -> viewModel.setSortOrder(order) },
                isShowPasteMenu = state.copiedFile != null,
                onPasteClick = { viewModel.pasteFile() }
            )
        }
    ) { paddingValues ->
        if (state.isLoading) {
            LoadingContent()
        } else {
            FileList(
                files = displayedFiles,
                onOpenFile = viewModel::openFile,
                onSaveInDownloads = { file ->
                    viewModel.downloadFileToDownloads(file)
                },
                onSaveInCustomDirectory = { file ->
                    viewModel.setFileToCustomDownloads(file)
                    directoryPickerLauncher.launch(null)
                },
                onRenameFile = { file ->
                    dialogState = DialogState.RenameFile(file)
                },
                onDeleteFile = viewModel::deleteFile,
                onCheckFileSelection = { state.selectedFiles.contains(it) },
                isSelectionModeActive = viewModel.getSelectionModeActive(),
                onSelectFile = { fileUri ->
                    viewModel.selectFile(fileUri)
                    Log.d("loadTest", "Run composable FileManagerScreen onSelectFile")
                },
                setSelectionModeActive = { viewModel.setSelectionModeActive() },
                onInfoFile = { file -> viewModel.showFileInfo(file) },
                modifier = Modifier.padding(paddingValues),
                onCopyFile = { file -> viewModel.copyFile(file) }
            )
        }

        state.errorMessage?.let { errorMessage ->
            ErrorDialog(
                errorMessage = errorMessage,
                onCloseErrorMessage = viewModel::clearErrorMessage
            )
        }

        state.cachedFileInfo?.let { (uri, mimeType) ->
            try {
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    setDataAndType(uri, mimeType ?: "*/*")
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(intent)
                viewModel.clearCacheFileInfo()
            } catch (e: Exception) {
                viewModel.setErrorMessage(e.message ?: "No app found to open this file type")
            }
        }

        if (state.isOperationInProgress) {
            ProgressDialog(
                operationType = state.operationType ?: OperationType.UPLOAD,
                fileName = state.operationFileName ?: "file",
                progress = state.operationProgress,
                onCancelClick = viewModel::cancelCurrentOperation
            )
        }

        state.fileInfoToShow?.let { file ->
            FileInfoDialog(
                file = file,
                onDismiss = { viewModel.hideFileInfo() }
            )
        }

        when (val currentDialog = dialogState) {
            is DialogState.RenameFile -> {
                FileNameInputDialog(
                    title = "Enter new name",
                    initialValue = currentDialog.file.name,
                    onDismiss = { dialogState = DialogState.None },
                    onConfirm = { newName ->
                        viewModel.renameFile(currentDialog.file.uri, newName)
                        dialogState = DialogState.None
                    }
                )
            }
            is DialogState.CreateFolder -> {
                FileNameInputDialog(
                    title = "Create Folder",
                    initialValue = "",
                    confirmButtonText = "Create",
                    onDismiss = { dialogState = DialogState.None },
                    onConfirm = { folderName ->
                        viewModel.createDirectory(name = folderName)
                        dialogState = DialogState.None
                    }
                )
            }
            DialogState.None -> {}
        }

        if (state.shouldNavigateBack) {
            onNavigateBack()
        }
    }
}

sealed class DialogState {
    data class RenameFile(val file: FileItem) : DialogState()
    data object CreateFolder : DialogState()
    data object None : DialogState()
}

private fun isSubsequence(query: String, text: String): Boolean {
    var j = 0
    for (i in query.indices) {
        val c = query[i]
        while (j < text.length && text[j] != c) {
            j++
        }
        if (j >= text.length) {
            return false
        }
        j++
    }
    return true
}

private fun levenshteinDistance(s1: String, s2: String): Int {
    val m = s1.length
    val n = s2.length
    val dp = Array(m + 1) { IntArray(n + 1) }

    for (i in 0..m) dp[i][0] = i
    for (j in 0..n) dp[0][j] = j

    for (i in 1..m) {
        for (j in 1..n) {
            dp[i][j] = if (s1[i-1] == s2[j-1]) {
                dp[i-1][j-1]
            } else {
                minOf(dp[i-1][j], dp[i][j-1], dp[i-1][j-1]) + 1
            }
        }
    }

    return dp[m][n]
}