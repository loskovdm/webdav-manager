package com.example.webdavmanager.file_manager.ui.component

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.webdavmanager.core.ui.theme.WebdavManagerTheme
import com.example.webdavmanager.file_manager.ui.model.FileItem

@Composable
fun FileList(
    files: List<FileItem>,
    onOpenFile: (file: FileItem) -> Unit,
    onSelectFile: (fileUri: String) -> Unit,
    onCheckFileSelection: (fileUri: String) -> Boolean,
    isSelectionModeActive: Boolean,
    setSelectionModeActive: () -> Unit,
    onSaveInDownloads: (file: FileItem) -> Unit,
    onSaveInCustomDirectory: (file: FileItem) -> Unit,
    onRenameFile: (file: FileItem) -> Unit,
    onDeleteFile: (fileUri: String) -> Unit,
    onInfoFile: (file: FileItem) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        items(files) { file ->
            FileItem(
                file = file,
                onFileClick = { onOpenFile(file) },
                onFileLongClick = {
                    setSelectionModeActive()
                    onSelectFile(file.uri)
                    Log.d("loadTest", "Run composable FileList onFileLongClick")
                },
                onSaveInDownloadClick = { onSaveInDownloads(file) },
                onSaveInCustomDirectoryClick = { onSaveInCustomDirectory(file) },
                onCopyClick = {},
                onMoveClick = {},
                onRenameClick = { onRenameFile(file) },
                onDeleteClick = { onDeleteFile(file.uri) },
                onFileSelect = { onSelectFile(file.uri) },
                isSelected = onCheckFileSelection(file.uri),
                isSelectionModeActive = isSelectionModeActive,
                onInfoClick = { onInfoFile(file) }
            )
        }
    }
}

@Preview
@Composable
fun PreviewFileList() {
    WebdavManagerTheme {
        FileList(
            files = listOf(
                FileItem(
                    name = "photo.png",
                    uri = "https://testServer/webdav/photo.png",
                    mimeType = "image/png",
                    isDirectory = false,
                    creationDate = null,
                    modifiedDate = null,
                    size = 1000
                ),
                FileItem(
                    name = "importantDoc.docx",
                    uri = "https://testServer/webdav/importantDoc.docx",
                    mimeType = "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                    isDirectory = false,
                    creationDate = null,
                    modifiedDate = null,
                    size = 200
                ),
                FileItem(
                    name = "dir",
                    uri = "https://testServer/webdav/dir/",
                    mimeType = null,
                    isDirectory = true,
                    creationDate = null,
                    modifiedDate = null,
                    size = 1000
                )
            ),
            onDeleteFile = {},
            onOpenFile = {},
            onRenameFile = {},
            onSaveInCustomDirectory = {},
            onSaveInDownloads = {},
            onSelectFile = {},
            onCheckFileSelection = { false },
            isSelectionModeActive = false,
            setSelectionModeActive = {},
            onInfoFile = {}
        )
    }
}