package com.example.webdavmanager.server_list.domain.repository

import com.example.webdavmanager.server_list.domain.model.ServerItem

interface ServerListRepository {
    suspend fun getServers(): List<ServerItem>?
    suspend fun deleteServerById(id: Int)
}