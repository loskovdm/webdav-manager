package io.github.loskovdm.webdavmanager.core.data.repository

import io.github.loskovdm.webdavmanager.core.data.model.Server

interface ServerRepository {
    suspend fun getServerList(): List<Server>
    suspend fun getServerById(id: Int): Server?
    suspend fun deleteServerById(id: Int)
    suspend fun insertServer(serverConfig: Server)
    suspend fun updateServer(serverConfig: Server)
}