package com.example.webdavmanager.file_manager.ui

import android.content.Intent
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.webdavmanager.core.ui.component.ErrorDialog
import com.example.webdavmanager.core.ui.component.LoadingContent
import com.example.webdavmanager.file_manager.ui.component.FileInfoDialog
import com.example.webdavmanager.file_manager.ui.component.FileList
import com.example.webdavmanager.file_manager.ui.component.FileManagerTopAppBar
import com.example.webdavmanager.file_manager.ui.component.FileNameInputDialog
import com.example.webdavmanager.file_manager.ui.component.ProgressDialog
import com.example.webdavmanager.file_manager.ui.model.FileItem

@Composable
fun FileManagerScreen(
    viewModel: FileManagerViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
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
        }
        else if (viewModel.canNavigateUp()) {
            viewModel.navigateUp()
        } else {
            onNavigateBack()
        }
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
                closeSelectionModeActive = { viewModel.setSelectionModeActive() },
                onDeleteCheckedFile = { viewModel.deleteCheckedFile() }
            )
        }
    ) { paddingValues ->
        if (state.isLoading) {
            LoadingContent()
        } else {
            FileList(
                files = state.fileList,
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
                modifier = Modifier.padding(paddingValues)
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
                        dialogState= DialogState.None
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
    }
}

sealed class DialogState {
    data class RenameFile(val file: FileItem) : DialogState()
    data object CreateFolder : DialogState()
    data object None : DialogState()
}