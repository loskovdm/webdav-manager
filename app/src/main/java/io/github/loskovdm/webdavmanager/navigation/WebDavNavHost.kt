package io.github.loskovdm.webdavmanager.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.github.loskovdm.webdavmanager.file_manager.ui.FileManagerScreen
import io.github.loskovdm.webdavmanager.navigation.NavDestination.FileManagerDestination
import io.github.loskovdm.webdavmanager.navigation.NavDestination.ServerConfigDestination
import io.github.loskovdm.webdavmanager.navigation.NavDestination.ServerListDestination
import io.github.loskovdm.webdavmanager.server_config.ServerConfigScreen
import io.github.loskovdm.webdavmanager.server_list.ServerListScreen

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
            ServerConfigScreen(
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
            FileManagerScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}