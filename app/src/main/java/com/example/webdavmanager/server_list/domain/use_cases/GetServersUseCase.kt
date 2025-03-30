package com.example.webdavmanager.server_list.domain.use_cases

import com.example.webdavmanager.server_list.domain.model.ServerItem
import com.example.webdavmanager.server_list.domain.repository.ServerListRepository
import javax.inject.Inject

class GetServersUseCase @Inject constructor(
    private val repository: ServerListRepository
) {
    suspend operator fun invoke(): List<ServerItem> = repository.getServers()
}