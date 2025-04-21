package com.example.webdavmanager.server_list.util

import com.example.webdavmanager.core.data.local.ServerEntity
import com.example.webdavmanager.server_list.model.ServerItem

fun ServerEntity.toServerItem(): ServerItem {
    return ServerItem(
        id = id,
        name = name,
        user = user
    )
}