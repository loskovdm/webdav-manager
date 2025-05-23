package io.github.loskovdm.webdavmanager.core.model

data class ServerModel(
    val id: Int,
    val name: String,
    val url: String,
    val user: String,
    val password: String
)
