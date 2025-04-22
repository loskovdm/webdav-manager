package com.example.webdavmanager.core.data.repository

import com.example.webdavmanager.core.data.local.ServerEntity

interface ServerRepository {
    suspend fun getServerList(): List<ServerEntity>
    suspend fun getServerById(id: Int): ServerEntity?
    suspend fun deleteServerById(id: Int)
    suspend fun insertServer(serverConfig: ServerEntity)
    suspend fun updateServer(serverConfig: ServerEntity)
}