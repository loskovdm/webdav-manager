package com.example.webdavmanager.server_config.domain.repository

import com.example.webdavmanager.server_config.domain.model.ServerConfig

interface ServerConfigRepository {
    suspend fun getServerConfigById(id: Int): ServerConfig?
    suspend fun insertServerConfig(server: ServerConfig)
    suspend fun updateServerConfig(server: ServerConfig)
}