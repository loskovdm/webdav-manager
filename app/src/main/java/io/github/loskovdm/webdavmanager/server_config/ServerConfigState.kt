package io.github.loskovdm.webdavmanager.server_config

data class ServerConfigState(
    val name: String = "",
    val url: String = "",
    val user: String = "",
    val password: String = "",
    val isNewConfig: Boolean = true,
    val isSaved: Boolean = true,
    val errorMessage: String? = null
)