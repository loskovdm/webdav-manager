package com.example.webdavmanager.server_config.ui.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.webdavmanager.R
import com.example.webdavmanager.core.ui.theme.WebdavManagerTheme

@Composable
fun ConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        icon = {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = null
            )
        },
        title = {
            Text(
                text = stringResource(R.string.unsaved_changes),
                style = MaterialTheme.typography.headlineSmall
            )
        },
        text = {
            Text(
                text = stringResource(R.string.unsaved_changes_confirmation_question),
                style = MaterialTheme.typography.bodyMedium
            )
        },
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.onSurface
                )
            ) {
                Text(
                    text = stringResource(R.string.ok),
                    style = MaterialTheme.typography.labelLarge
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.onSurface
                )
            ) {
                Text(
                    text = stringResource(R.string.cancel),
                    style = MaterialTheme.typography.labelLarge
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.surface,
        iconContentColor = MaterialTheme.colorScheme.error,
        titleContentColor = MaterialTheme.colorScheme.onSurface,
        textContentColor = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Preview
@Composable
fun PreviewConfirmationDialog() {
    WebdavManagerTheme {
        ConfirmationDialog(
            onConfirm = {},
            onDismiss = {}
        )
    }
}