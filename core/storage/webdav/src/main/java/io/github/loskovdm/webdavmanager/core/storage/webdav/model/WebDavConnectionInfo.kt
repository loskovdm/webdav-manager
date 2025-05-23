package io.github.loskovdm.webdavmanager.core.storage.webdav.model

import io.github.loskovdm.webdavmanager.core.model.ServerModel

data class WebDavConnectionInfo(
    val url: String,
    val user: String,
    val password: String
)

fun ServerModel.asWebDavConnectionInfo() = WebDavConnectionInfo(
    url = url,
    user = user,
    password = password
)
