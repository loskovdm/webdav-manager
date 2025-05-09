package com.example.webdavmanager.file_manager.ui.util

import com.example.webdavmanager.core.data.local.ServerEntity
import com.example.webdavmanager.file_manager.data.model.WebDavConnectionInfo
import com.example.webdavmanager.file_manager.ui.model.ServerConnectionInfo

fun ServerEntity.toServerConnectionInfo(): ServerConnectionInfo {
    return ServerConnectionInfo(
        url = url,
        user = user,
        password = password
    )
}

fun ServerConnectionInfo.toWebDavConnectionInfo(): WebDavConnectionInfo {
    return WebDavConnectionInfo(
        url = url,
        user = user,
        password = password
    )
}