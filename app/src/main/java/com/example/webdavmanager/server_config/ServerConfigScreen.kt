package com.example.webdavmanager.server_config

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.webdavmanager.core.ui.component.ErrorDialog
import com.example.webdavmanager.server_config.component.ConfirmationDialog
import com.example.webdavmanager.server_config.component.ServerConfigForm
import com.example.webdavmanager.server_config.component.ServerConfigTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServerConfigScreen(
    viewModel: ServerConfigViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateBackWithChanges:() -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var showConfirmationDialog = remember { mutableStateOf(false) }

    if (showConfirmationDialog.value) {
        ConfirmationDialog(
            onConfirm = { onNavigateBack() },
            onDismiss = { showConfirmationDialog.value = false }
        )
    }

    BackHandler(enabled = true) {
        if (!state.isSaved) {
            showConfirmationDialog.value = true
        } else {
            onNavigateBack()
        }
    }

    Scaffold(
        topBar = {
            ServerConfigTopAppBar(
                isNewConfig = state.isNewConfig,
                onSaveConfig = viewModel::saveConfig,
                onNavigateBack = {
                    if (!state.isSaved) {
                        showConfirmationDialog.value = true
                    } else {
                        onNavigateBack()
                    }
                },
                onNavigateBackWithChanges = { onNavigateBackWithChanges() }
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
            validateInput = viewModel::validateInput,
            modifier = Modifier.padding(paddingValues)
        )
    }
    state.errorMessage?.let { errorMessage ->
        ErrorDialog(
            errorMessage = errorMessage,
            onCloseErrorMessage = viewModel::clearErrorMessage
        )
    }
}