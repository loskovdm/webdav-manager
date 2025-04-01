package com.example.webdavmanager.server_config.domain.repository

import com.example.webdavmanager.server_config.domain.model.ServerConfig

interface ServerConfigRepository {
    suspend fun getServer(id: Int): ServerConfig
    suspend fun insertServer(server: ServerConfig)
    suspend fun updateServer(server: ServerConfig)
}