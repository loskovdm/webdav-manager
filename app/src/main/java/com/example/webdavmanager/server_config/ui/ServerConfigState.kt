package com.example.webdavmanager.server_config.ui

import com.example.webdavmanager.server_config.domain.model.ServerConfig

sealed interface ServerConfigState {
    object Loading : ServerConfigState
    data class Error(val message: String) : ServerConfigState
    data class Editing(
        val serverConfig: ServerConfig,
        val isNewServer: Boolean
    ) : ServerConfigState
}