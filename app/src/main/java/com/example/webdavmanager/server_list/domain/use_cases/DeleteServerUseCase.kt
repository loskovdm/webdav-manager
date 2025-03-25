package com.example.webdavmanager.server_list.domain.use_cases

import com.example.webdavmanager.server_list.domain.repository.ServerListRepository

class DeleteServerUseCase(
    private val repository: ServerListRepository
) {
    suspend operator fun invoke(id: Int) = repository.deleteServerById(id)
}