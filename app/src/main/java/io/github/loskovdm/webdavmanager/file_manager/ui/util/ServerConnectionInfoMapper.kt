package io.github.loskovdm.webdavmanager.file_manager.ui.util

import io.github.loskovdm.webdavmanager.core.data.local.ServerEntity
import io.github.loskovdm.webdavmanager.file_manager.data.model.WebDavConnectionInfo
import io.github.loskovdm.webdavmanager.file_manager.ui.model.ServerConnectionInfo

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