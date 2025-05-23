package io.github.loskovdm.webdavmanager.feature.filemanager.model

import io.github.loskovdm.webdavmanager.core.model.ServerModel

data class ServerConnectionInfo(
    val url: String,
    val user: String,
    val password: String
)
