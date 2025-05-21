package io.github.loskovdm.webdavmanager.server_list

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.loskovdm.webdavmanager.core.ui.component.ErrorDialog
import io.github.loskovdm.webdavmanager.server_list.component.ServerList
import io.github.loskovdm.webdavmanager.server_list.component.ServerListEmpty
import io.github.loskovdm.webdavmanager.server_list.component.ServerListFloatingActionButton
import io.github.loskovdm.webdavmanager.server_list.component.ServerListTopAppBar


@Composable
fun ServerListScreen(
    viewModel: ServerListViewModel = hiltViewModel(),
    onNavigateToServerFileManager: (Int) -> Unit,
    onNavigateToServerConfig: (Int) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            ServerListTopAppBar()
        },
        floatingActionButton = {
            ServerListFloatingActionButton(
                onClick = { serverId ->
                    onNavigateToServerConfig(serverId)
                }
            )
        }
    ) { paddingValues ->
        if (state.serverList.isEmpty() && state.isLoaded) {
            ServerListEmpty(
                modifier = Modifier.padding(paddingValues)
            )
        } else {
            ServerList(
                servers = state.serverList,
                onNavigateToServerFileManager = { serverId ->
                    onNavigateToServerFileManager(serverId)
                },
                onNavigateToServerConfig = { serverId ->
                    onNavigateToServerConfig(serverId)
                },
                onDeleteServer = viewModel::deleteServer,
                modifier = Modifier.padding(paddingValues)
            )
        }
    }

    state.errorMessage?.let { errorMessage ->
        ErrorDialog(
            errorMessage = errorMessage,
            onCloseErrorMessage = viewModel::clearErrorMessage
        )
    }
}