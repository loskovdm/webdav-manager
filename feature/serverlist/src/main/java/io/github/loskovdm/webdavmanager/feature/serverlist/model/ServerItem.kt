package io.github.loskovdm.webdavmanager.feature.serverlist.model

import io.github.loskovdm.webdavmanager.core.data.model.ServerModel

data class ServerItem(
    val id: Int,
    val name: String,
    val user: String
)

fun ServerModel.asExternalModel() = ServerItem(
    id = id,
    name = name,
    user = user
)
