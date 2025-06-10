package io.github.loskovdm.webdavmanager.core.data.model

import io.github.loskovdm.webdavmanager.core.database.ServerEntity
import io.github.loskovdm.webdavmanager.core.storage.webdav.model.WebDavConnectionInfo

data class ServerModel(
    val id: Int,
    val name: String,
    val url: String,
    val user: String,
    val password: String
)

fun ServerModel.asWebDavConnectionInfo() =
    WebDavConnectionInfo(
        url = url,
        user = user,
        password = password
    )

fun ServerModel.asServerEntity() =
    ServerEntity(
        id = id,
        name = name,
        url = url,
        user = user,
        password = password
    )

fun ServerEntity.asExternalModel() =
    ServerModel(
        id = id,
        name = name,
        url = url,
        user = user,
        password = password
    )
