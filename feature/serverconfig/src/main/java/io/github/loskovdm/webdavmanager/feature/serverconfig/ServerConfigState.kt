package io.github.loskovdm.webdavmanager.feature.serverconfig

data class ServerConfigState(
    val id: Int = 0,
    val name: String = "",
    val url: String = "",
    val user: String = "",
    val password: String = "",
    val isNewConfig: Boolean = true,
    val isSaved: Boolean = true,
    val errorMessage: String? = null
)