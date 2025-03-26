package com.example.webdavmanager.server_list.domain.use_cases

import com.example.webdavmanager.server_list.domain.repository.ServerListRepository
import javax.inject.Inject

class DeleteServerUseCase @Inject constructor(
    private val repository: ServerListRepository
) {
    suspend operator fun invoke(id: Int) = repository.deleteServerById(id)
}