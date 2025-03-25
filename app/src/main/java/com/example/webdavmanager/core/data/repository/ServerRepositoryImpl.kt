package com.example.webdavmanager.core.data.repository

import com.example.webdavmanager.core.data.local.ServerDao
import com.example.webdavmanager.core.data.local.Server
import com.example.webdavmanager.core.data.security.PasswordEncryptor
import com.example.webdavmanager.server_list.domain.model.ServerItem
import com.example.webdavmanager.server_list.domain.repository.ServerListRepository

class ServerRepositoryImpl(
    private val serverDao: ServerDao,
    private val encryptor: PasswordEncryptor
) : ServerListRepository {

    suspend fun getServer(id: Int): Server? {
        val server = serverDao.getServerById(id)
        return server?.let {
            val decryptedPassword = encryptor.decrypt(it.password)
            it.copy(password = decryptedPassword)
        }
    }

    override suspend fun getServers(): List<ServerItem>? {
        return serverDao.getAll().map { server ->
            ServerItem(
                id = server.id,
                name = server.name,
                user = server.user
            )
        }
    }

    suspend fun insertServer(server: Server) {
        val encryptedPassword = encryptor.encrypt(server.password)
        val serverToInsert =  server.copy(password = encryptedPassword)
        serverDao.insertServer(serverToInsert)
    }

    override suspend fun deleteServerById(id: Int) {
        val server = serverDao.getServerById(id)
        server?.let {
            serverDao.deleteServer(it)
        }
    }
}