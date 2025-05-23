package io.github.loskovdm.webdavmanager.feature.serverlist.model

import io.github.loskovdm.webdavmanager.core.model.ServerModel

data class ServerItem(
    val id: Int,
    val name: String,
    val user: String
)

fun ServerModel.asServerItem() = ServerItem(
    id = id,
    name = name,
    user = user
)
