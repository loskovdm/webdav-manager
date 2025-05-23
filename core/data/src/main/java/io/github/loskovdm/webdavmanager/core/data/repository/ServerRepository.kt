package io.github.loskovdm.webdavmanager.core.data.repository

import io.github.loskovdm.webdavmanager.core.model.ServerModel

interface ServerRepository {
    suspend fun getServerList(): List<ServerModel>
    suspend fun getServerById(id: Int): ServerModel?
    suspend fun deleteServerById(id: Int)
    suspend fun insertServer(serverConfig: ServerModel)
    suspend fun updateServer(serverConfig: ServerModel)
}