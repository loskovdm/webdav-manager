package com.example.webdavmanager.server_config.domain.use_cases

import com.example.webdavmanager.server_config.domain.model.ServerConfig
import com.example.webdavmanager.server_config.domain.repository.ServerConfigRepository
import javax.inject.Inject

class SaveServerConfigUseCase @Inject constructor(
    private val repository: ServerConfigRepository
) {
    suspend operator fun invoke(serverConfig: ServerConfig) = if (serverConfig.id == 0) {
        repository.insertServerConfig(serverConfig)
    } else {
        repository.updateServerConfig(serverConfig)
    }
}