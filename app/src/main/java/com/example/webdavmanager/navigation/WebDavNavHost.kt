package com.example.webdavmanager.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.webdavmanager.navigation.NavDestination.ServerConfigDestination
import com.example.webdavmanager.navigation.NavDestination.ServerListDestination
import com.example.webdavmanager.server_config.ServerConfigScreen
import com.example.webdavmanager.server_list.ServerListScreen

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
    }
}