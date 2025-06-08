package io.github.loskovdm.webdavmanager.feature.serverconfig.model

import io.github.loskovdm.webdavmanager.core.data.model.Server

data class ServerConfig(
    val id: Int = 0,
    val name: String,
    val url: String,
    val user: String,
    val password: String
)

fun Server.asServerConfig() = ServerConfig(
    id = id,
    name = name,
    url = url,
    user = user,
    password = password
)

fun ServerConfig.asExternalModel() = Server(
    id = id,
    name = name,
    url = url,
    user = user,
    password = password
)