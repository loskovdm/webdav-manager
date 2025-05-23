package io.github.loskovdm.webdavmanager.feature.serverconfig.model

import io.github.loskovdm.webdavmanager.core.model.ServerModel

data class ServerConfig(
    val id: Int = 0,
    val name: String,
    val url: String,
    val user: String,
    val password: String
)

fun ServerModel.asServerConfig() = ServerConfig(
    id = id,
    name = name,
    url = url,
    user = user,
    password = password
)

fun ServerConfig.asExternalModel() = ServerModel(
    id = id,
    name = name,
    url = url,
    user = user,
    password = password
)