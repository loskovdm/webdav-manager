package io.github.loskovdm.webdavmanager.server_config.util

import io.github.loskovdm.webdavmanager.core.data.local.ServerEntity
import io.github.loskovdm.webdavmanager.server_config.model.ServerConfig

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