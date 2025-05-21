package io.github.loskovdm.webdavmanager.core.data.repository

import io.github.loskovdm.webdavmanager.core.data.local.ServerDao
import io.github.loskovdm.webdavmanager.core.data.local.ServerEntity
import io.github.loskovdm.webdavmanager.core.data.security.PasswordEncryptor
import javax.inject.Inject

class ServerRepositoryImpl @Inject constructor(
    private val serverDao: ServerDao,
    private val encryptor: PasswordEncryptor
) : ServerRepository {

    override suspend fun deleteServerById(id: Int) {
        val server = serverDao.getServerById(id)
        server?.let {
            serverDao.deleteServer(it)
        }
    }

    override suspend fun getServerList(): List<ServerEntity> {
        return serverDao.getAll()
    }

    override suspend fun getServerById(id: Int): ServerEntity? {
        return serverDao.getServerById(id)?.let { serverEntity ->
            serverEntity.copy(
                password = encryptor.decrypt(serverEntity.password)
            )
        }
    }

    override suspend fun insertServer(serverConfig: ServerEntity) {
        serverDao.insertServer(serverConfig.copy(
            password = encryptor.encrypt(serverConfig.password)
        ))
    }

    override suspend fun updateServer(serverConfig: ServerEntity) {
        serverDao.updateServer(serverConfig.copy(
            password = encryptor.encrypt(serverConfig.password)
        ))
    }
}