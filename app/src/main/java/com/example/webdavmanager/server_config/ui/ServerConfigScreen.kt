package com.example.webdavmanager.server_config.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.webdavmanager.core.ui.component.ErrorDialog
import com.example.webdavmanager.server_config.ui.component.ServerConfigForm
import com.example.webdavmanager.server_config.ui.component.ServerConfigTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServerConfigScreen(
    viewModel: ServerConfigViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            ServerConfigTopAppBar(
                isNewConfig = state.isNewConfig,
                isSaved = state.isSaved,
                onSaveConfig = viewModel::saveConfig
            )
        }
    ) { paddingValues ->
        ServerConfigForm(
            name = state.name,
            url = state.url,
            user = state.user,
            password = state.password,
            onChangeName = viewModel::updateName,
            onChangeUrl = viewModel::updateUrl,
            onChangeUser = viewModel::updateUser,
            onChangePassword = viewModel::updatePassword,
            modifier = Modifier
                .padding(paddingValues)
        )
    }
    state.errorMessage?.let { errorMessage ->
        ErrorDialog(
            errorMessage = errorMessage,
            onCloseErrorMessage = viewModel::clearErrorMessage
        )
    }
}