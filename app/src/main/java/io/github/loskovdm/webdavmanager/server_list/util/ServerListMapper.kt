package io.github.loskovdm.webdavmanager.server_list.util

import io.github.loskovdm.webdavmanager.core.data.local.ServerEntity
import io.github.loskovdm.webdavmanager.server_list.model.ServerItem

fun ServerEntity.toServerItem(): ServerItem {
    return ServerItem(
        id = id,
        name = name,
        user = user
    )
}