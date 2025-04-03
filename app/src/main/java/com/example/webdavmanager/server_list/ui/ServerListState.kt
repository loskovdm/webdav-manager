package com.example.webdavmanager.server_list.ui

import com.example.webdavmanager.server_list.domain.model.ServerItem

data class ServerListState(
    val serverList: List<ServerItem> = emptyList<ServerItem>(),
    val isLoaded: Boolean = false,
    val errorMessage: String? = null
)