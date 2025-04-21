package com.example.webdavmanager.core.data.repository

import com.example.webdavmanager.core.data.local.ServerDao
import com.example.webdavmanager.core.data.local.ServerEntity
import com.example.webdavmanager.core.data.security.PasswordEncryptor
import javax.inject.Inject

class ServerRepository @Inject constructor(
    private val serverDao: ServerDao,
    private val encryptor: PasswordEncryptor
) {

    suspend fun deleteServerById(id: Int) {
        val server = serverDao.getServerById(id)
        server?.let {
            serverDao.deleteServer(it)
        }
    }

    suspend fun getServerItemList(): List<ServerEntity> {
        return serverDao.getAll()
    }

    suspend fun getServerConfigById(id: Int): ServerEntity? {
        return serverDao.getServerById(id)?.let { serverEntity ->
            serverEntity.copy(
                password = encryptor.decrypt(serverEntity.password)
            )
        }
    }

    suspend fun insertServerConfig(serverConfig: ServerEntity) {
        serverDao.insertServer(serverConfig.copy(
            password = encryptor.encrypt(serverConfig.password)
        ))
    }

    suspend fun updateServerConfig(serverConfig: ServerEntity) {
        serverDao.updateServer(serverConfig.copy(
            password = encryptor.encrypt(serverConfig.password)
        ))
    }
}