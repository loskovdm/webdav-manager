package io.github.loskovdm.webdavmanager.navigation

import kotlinx.serialization.Serializable

sealed interface NavDestination {
    @Serializable
    object ServerListDestination: NavDestination

    @Serializable
    data class ServerConfigDestination(val serverId: Int): NavDestination

    @Serializable
    data class FileManagerDestination(val serverId: Int): NavDestination
}