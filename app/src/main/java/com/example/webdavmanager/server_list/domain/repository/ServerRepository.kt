package com.example.webdavmanager.server_list.domain.repository

import com.example.webdavmanager.server_list.domain.model.ServerItem

interface ServerRepository {
    suspend fun getServers(): List<ServerItem>
}