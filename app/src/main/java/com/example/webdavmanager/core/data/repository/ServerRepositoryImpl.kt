package com.example.webdavmanager.core.data.repository

import com.example.webdavmanager.core.data.local.ServerDao
import com.example.webdavmanager.core.data.mapper.toServerConfig
import com.example.webdavmanager.core.data.mapper.toServerEntity
import com.example.webdavmanager.core.data.mapper.toServerItem
import com.example.webdavmanager.core.data.security.PasswordEncryptor
import com.example.webdavmanager.server_config.domain.model.ServerConfig
import com.example.webdavmanager.server_config.domain.repository.ServerConfigRepository
import com.example.webdavmanager.server_list.domain.model.ServerItem
import com.example.webdavmanager.server_list.domain.repository.ServerListRepository
import javax.inject.Inject

class ServerRepositoryImpl @Inject constructor(
    private val serverDao: ServerDao,
    private val encryptor: PasswordEncryptor
) : ServerListRepository, ServerConfigRepository {

    override suspend fun deleteServerById(id: Int) {
        val server = serverDao.getServerById(id)
        server?.let {
            serverDao.deleteServer(it)
        }
    }

    override suspend fun getServerItemList(): List<ServerItem> {
        return serverDao.getAll().map { it.toServerItem() }
    }

    override suspend fun getServerConfigById(id: Int): ServerConfig? {
        return serverDao.getServerById(id)?.let { serverEntity ->
            serverEntity.toServerConfig().copy(
                password = encryptor.decrypt(serverEntity.password)
            )
        }
    }

    override suspend fun insertServerConfig(serverConfig: ServerConfig) {
        serverDao.insertServer(serverConfig.toServerEntity().copy(
            password = encryptor.encrypt(serverConfig.password)
        ))
    }

    override suspend fun updateServerConfig(serverConfig: ServerConfig) {
        serverDao.updateServer(serverConfig.toServerEntity().copy(
            password = encryptor.encrypt(serverConfig.password)
        ))
    }
}