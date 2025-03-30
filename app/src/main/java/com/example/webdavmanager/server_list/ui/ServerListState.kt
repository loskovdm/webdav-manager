package com.example.webdavmanager.server_list.ui

import com.example.webdavmanager.server_list.domain.model.ServerItem

sealed interface ServerListState {
    object Loading : ServerListState
    data class Success(val servers: List<ServerItem>) : ServerListState
    data class Error(val message: String) : ServerListState
    object Empty : ServerListState
}