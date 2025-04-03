package com.example.webdavmanager.server_config.ui.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
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
fun ErrorDialog(
    errorMessage: String,
    onClearErrorMessage: () -> Unit
) {
    AlertDialog(
        icon = {
            Icon(
                imageVector = Icons.Default.Error,
                contentDescription = null
            )
        },
        title = {
            Text(
                text = stringResource(R.string.error),
                style = MaterialTheme.typography.headlineSmall
            )
        },
        text = {
            Text(
                text = errorMessage,
                style = MaterialTheme.typography.bodyMedium
            )
        },
        onDismissRequest = { onClearErrorMessage },
        confirmButton = {
            TextButton(
                onClick = { onClearErrorMessage },
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
        containerColor = MaterialTheme.colorScheme.surface,
        iconContentColor = MaterialTheme.colorScheme.error,
        titleContentColor = MaterialTheme.colorScheme.onSurface,
        textContentColor = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Preview
@Composable
fun PreviewErrorDialog() {
    WebdavManagerTheme {
        ErrorDialog(
            errorMessage = "Error message",
            onClearErrorMessage = {}
        )
    }
}