package io.github.loskovdm.webdavmanager.core.data.model

import io.github.loskovdm.webdavmanager.core.database.ServerEntity
import io.github.loskovdm.webdavmanager.core.storage.webdav.model.WebDavConnectionInfo

data class Server(
    val id: Int,
    val name: String,
    val url: String,
    val user: String,
    val password: String
)

fun Server.asWebDavConnectionInfo() =
    WebDavConnectionInfo(
        url = url,
        user = user,
        password = password
    )

fun Server.asServerEntity() =
    ServerEntity(
        id = id,
        name = name,
        url = url,
        user = user,
        password = password
    )

fun ServerEntity.asExternalModel() =
    Server(
        id = id,
        name = name,
        url = url,
        user = user,
        password = password
    )
