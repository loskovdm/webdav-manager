package com.example.webdavmanager.server_config.util

import com.example.webdavmanager.core.data.local.ServerEntity
import com.example.webdavmanager.server_config.model.ServerConfig

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