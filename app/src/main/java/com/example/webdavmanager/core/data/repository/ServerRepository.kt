package com.example.webdavmanager.core.data.repository

import com.example.webdavmanager.core.data.local.ServerDao
import com.example.webdavmanager.core.data.local.Server
import com.example.webdavmanager.core.data.security.PasswordEncryptor

class ServerRepository(
    private val serverDao: ServerDao,
    private val encryptor: PasswordEncryptor
) {
    suspend fun insertServer(server: Server) {
        val encryptedPassword = encryptor.encrypt(server.password)
        val serverToInsert =  server.copy(password = encryptedPassword)
        serverDao.insertServer(serverToInsert)
    }

    suspend fun getServer(id: Int): Server? {
        val server = serverDao.getServerById(id)
        return server?.let {
            val decryptedPassword = encryptor.decrypt(it.password)
            it.copy(password = decryptedPassword)
        }
    }
}