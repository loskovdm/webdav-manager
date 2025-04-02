package com.example.webdavmanager.core.data.mapper

import com.example.webdavmanager.core.data.local.ServerEntity
import com.example.webdavmanager.server_config.domain.model.ServerConfig
import com.example.webdavmanager.server_list.domain.model.ServerItem

fun ServerEntity.toServerConfig(): ServerConfig {
    return ServerConfig(
        id = id,
        name = name,
        url = url,
        user = user,
        password = password
    )
}

fun ServerConfig.toServerEntity(): ServerEntity {
    return ServerEntity(
        id = id,
        name = name,
        url = url,
        user = user,
        password = password
    )
}

fun ServerEntity.toServerItem(): ServerItem {
    return ServerItem(
        id = id,
        name = name,
        user = user
    )
}