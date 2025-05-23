package io.github.loskovdm.webdavmanager.feature.serverconfig.component

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
import androidx.compose.ui.res.stringResource
import io.github.loskovdm.webdavmanager.core.ui.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServerConfigTopAppBar(
    isNewConfig: Boolean,
    onSaveConfig: () -> Boolean,
    onNavigateBack: () -> Unit,
    onNavigateBackWithChanges: () -> Unit
) {
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
                onClick = { onNavigateBack() }
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
                    if (onSaveConfig()) onNavigateBackWithChanges()
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
