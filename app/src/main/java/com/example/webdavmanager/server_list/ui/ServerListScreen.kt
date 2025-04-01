package com.example.webdavmanager.server_list.ui

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.webdavmanager.core.ui.component.ErrorContent
import com.example.webdavmanager.core.ui.component.LoadingContent
import com.example.webdavmanager.server_list.ui.component.ServerList
import com.example.webdavmanager.server_list.ui.component.ServerListEmpty


@Composable
fun ServerListScreen(
    viewModel: ServerListViewModel = hiltViewModel()
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    when (uiState) {
        is ServerListState.Loading -> LoadingContent()
        is ServerListState.Empty -> ServerListEmpty()
        is ServerListState.Success -> ServerList(
            (uiState as ServerListState.Success).servers,
            onDeleteServer = { id -> viewModel.deleteServer(id) }
        )
        is ServerListState.Error -> ErrorContent((uiState as ServerListState.Error).message)
    }
}