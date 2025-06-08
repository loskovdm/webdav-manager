package io.github.loskovdm.webdavmanager.core.data.repository

import io.github.loskovdm.webdavmanager.core.database.ServerDao
import io.github.loskovdm.webdavmanager.core.data.model.Server
import io.github.loskovdm.webdavmanager.core.data.model.asExternalModel
import io.github.loskovdm.webdavmanager.core.data.model.asServerEntity
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

    override suspend fun getServerList(): List<Server> {
        return serverDao.getAll().map { it.asExternalModel() }
    }

    override suspend fun getServerById(id: Int): Server? {
        return serverDao.getServerById(id)?.let { serverEntity ->
            serverEntity.copy(
                password = encryptor.decrypt(serverEntity.password)
            ).asExternalModel()
        }
    }

    override suspend fun insertServer(serverConfig: Server) {
        serverDao.insertServer(serverConfig.copy(
            password = encryptor.encrypt(serverConfig.password)
        ).asServerEntity())
    }

    override suspend fun updateServer(serverConfig: Server) {
        serverDao.updateServer(serverConfig.copy(
            password = encryptor.encrypt(serverConfig.password)
        ).asServerEntity())
    }
}