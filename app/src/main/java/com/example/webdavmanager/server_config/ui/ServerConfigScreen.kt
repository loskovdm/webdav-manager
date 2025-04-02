package com.example.webdavmanager.server_config.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.webdavmanager.core.ui.component.ErrorContent
import com.example.webdavmanager.core.ui.component.LoadingContent
import com.example.webdavmanager.server_config.ui.component.ServerConfig

@Composable
fun ServerConfigScreen(
    viewModel: ServerConfigViewModel = hiltViewModel()
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    when (uiState) {
        is ServerConfigState.Loading -> LoadingContent()
        is ServerConfigState.Error -> ErrorContent((uiState as ServerConfigState.Error).message)
        is ServerConfigState.Editing -> ServerConfig(
            serverConfig = (uiState as ServerConfigState.Editing).serverConfig,
            isNewServer = (uiState as ServerConfigState.Editing).isNewServer,
            onSaveClick = { viewModel.saveServerConfig() },
            onNavigateUp = {} // TODO: Implement navigation
        )
    }
}