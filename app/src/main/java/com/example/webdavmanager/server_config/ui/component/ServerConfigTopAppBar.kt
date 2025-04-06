package com.example.webdavmanager.server_config.ui.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.webdavmanager.R
import com.example.webdavmanager.core.ui.theme.WebdavManagerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServerConfigTopAppBar(
    isNewConfig: Boolean,
    isSaved: Boolean,
    onSaveConfig: () -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateBackWithChanges: () -> Unit
) {

    var showConfirmationDialog = remember { mutableStateOf(false) }
    if (showConfirmationDialog.value) {
        ConfirmationDialog(
            onConfirm = {
                onNavigateBack()
            },
            onDismiss = {
                showConfirmationDialog.value = false
            }
        )
    }

    TopAppBar(
        title = {
            Text(
                text = if (isNewConfig) {
                    stringResource(R.string.create_server)
                } else {
                    stringResource(R.string.edit_server)
                },
                style = MaterialTheme.typography.titleLarge
            )
        },
        navigationIcon = {
            IconButton(
                onClick = {
                    if (!isSaved) {
                        showConfirmationDialog.value = true
                    } else {
                        onNavigateBack()
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.navigate_back)
                )
            }
        },
        actions = {
            IconButton(
                onClick = {
                    onSaveConfig()
                    onNavigateBackWithChanges()
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Save,
                    contentDescription = stringResource(R.string.save_configuration)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}

@Preview
@Composable
fun FirstPreviewServerConfigTopAppBar() {
    WebdavManagerTheme {
        ServerConfigTopAppBar(
            isNewConfig = true,
            isSaved = true,
            onSaveConfig = {},
            onNavigateBack = {},
            onNavigateBackWithChanges = {}
        )
    }
}

@Preview
@Composable
fun SecondPreviewServerConfigTopAppBar() {
    WebdavManagerTheme {
        ServerConfigTopAppBar(
            isNewConfig = false,
            isSaved = true,
            onSaveConfig = {},
            onNavigateBack = {},
            onNavigateBackWithChanges = {}
        )
    }
}