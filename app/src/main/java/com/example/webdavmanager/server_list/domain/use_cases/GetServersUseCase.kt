package com.example.webdavmanager.server_list.domain.use_cases

import com.example.webdavmanager.server_list.domain.model.ServerItem
import com.example.webdavmanager.server_list.domain.repository.ServerListRepository

class GetServersUseCase(
    private val repository: ServerListRepository
) {
    suspend operator fun invoke(): List<ServerItem>? = repository.getServers()
}