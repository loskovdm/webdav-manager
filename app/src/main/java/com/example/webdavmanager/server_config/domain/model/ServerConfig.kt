package com.example.webdavmanager.server_config.domain.model

data class ServerConfig(
    val id: Int = 0,
    val name: String,
    val url: String,
    val user: String,
    val password: String
)
