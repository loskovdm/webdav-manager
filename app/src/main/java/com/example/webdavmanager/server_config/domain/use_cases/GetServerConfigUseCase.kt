package com.example.webdavmanager.server_config.domain.use_cases

import com.example.webdavmanager.server_config.domain.model.ServerConfig
import com.example.webdavmanager.server_config.domain.repository.ServerConfigRepository
import javax.inject.Inject

class GetServerConfigUseCase @Inject constructor(
    private val repository: ServerConfigRepository
) {
    suspend operator fun invoke(id: Int): ServerConfig = if (id == 0) {
        ServerConfig(0, "", "", "", "")
    } else {
        repository.getServerConfigById(id) ?: ServerConfig(0, "", "", "", "")
    }
}