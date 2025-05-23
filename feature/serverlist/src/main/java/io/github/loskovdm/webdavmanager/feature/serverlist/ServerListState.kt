package io.github.loskovdm.webdavmanager.feature.serverlist

import io.github.loskovdm.webdavmanager.feature.serverlist.model.ServerItem

data class ServerListState(
    val serverList: List<ServerItem> = emptyList<ServerItem>(),
    val isLoaded: Boolean = false,
    val errorMessage: String? = null
)