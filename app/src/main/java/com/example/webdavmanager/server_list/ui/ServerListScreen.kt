package com.example.webdavmanager.server_list.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.webdavmanager.core.ui.component.ErrorDialog
import com.example.webdavmanager.server_list.ui.component.ServerList
import com.example.webdavmanager.server_list.ui.component.ServerListEmpty
import com.example.webdavmanager.server_list.ui.component.ServerListFloatingActionButton
import com.example.webdavmanager.server_list.ui.component.ServerListTopAppBar


@Composable
fun ServerListScreen(
    viewModel: ServerListViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            ServerListTopAppBar()
        },
        floatingActionButton = {
            ServerListFloatingActionButton(
                onClick = {} // TODO: Implement navigation
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