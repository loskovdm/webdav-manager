package io.github.loskovdm.webdavmanager.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import io.github.loskovdm.webdavmanager.feature.filemanager.FileManagerScreen
import io.github.loskovdm.webdavmanager.navigation.NavDestination.FileManagerDestination
import io.github.loskovdm.webdavmanager.navigation.NavDestination.ServerConfigDestination
import io.github.loskovdm.webdavmanager.navigation.NavDestination.ServerListDestination
import io.github.loskovdm.webdavmanager.feature.serverconfig.ServerConfigScreen
import io.github.loskovdm.webdavmanager.feature.serverlist.ServerListScreen

@Composable
fun WebDavNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = ServerListDestination,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None }
    ) {
        composable<ServerListDestination> {
            ServerListScreen(
                onNavigateToServerFileManager = { serverId ->
                    navController.navigate(route = FileManagerDestination(serverId = serverId))
                },
                onNavigateToServerConfig = { serverId ->
                    navController.navigate(route = ServerConfigDestination(serverId = serverId))
                }
            )
        }
        composable<ServerConfigDestination> {
            val serverId = it.toRoute<ServerConfigDestination>().serverId
            ServerConfigScreen(
                serverId = serverId,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateBackWithChanges = {
                    navController.navigate(ServerListDestination) {
                        popUpTo(ServerListDestination) {
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable<FileManagerDestination> {
            val serverId = it.toRoute<FileManagerDestination>().serverId
            FileManagerScreen(
                serverId = serverId,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}