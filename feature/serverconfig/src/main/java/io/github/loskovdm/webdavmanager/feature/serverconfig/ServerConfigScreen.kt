package io.github.loskovdm.webdavmanager.feature.serverconfig

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.loskovdm.webdavmanager.core.ui.component.ErrorDialog
import io.github.loskovdm.webdavmanager.feature.serverconfig.component.ConfirmationDialog
import io.github.loskovdm.webdavmanager.feature.serverconfig.component.ServerConfigForm
import io.github.loskovdm.webdavmanager.feature.serverconfig.component.ServerConfigTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServerConfigScreen(
    viewModel: ServerConfigViewModel = hiltViewModel(),
    serverId: Int,
    onNavigateBack: () -> Unit,
    onNavigateBackWithChanges:() -> Unit
) {
    LaunchedEffect(serverId) {
        viewModel.setServerId(serverId)
    }

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