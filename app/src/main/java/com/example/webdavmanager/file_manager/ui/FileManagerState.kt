package com.example.webdavmanager.file_manager.ui

import com.example.webdavmanager.file_manager.ui.model.FileItem
import com.example.webdavmanager.file_manager.ui.model.ServerConnectionInfo

data class FileManagerState(
    val serverConnectionInfo: ServerConnectionInfo,
    val currentDirectoryUri: String,
    val fileList: List<FileItem> = emptyList<FileItem>(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)