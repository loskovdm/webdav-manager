package io.github.loskovdm.webdavmanager.core.ui.component

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
import io.github.loskovdm.webdavmanager.core.ui.R
import io.github.loskovdm.webdavmanager.core.ui.theme.WebdavManagerTheme

@Composable
fun ErrorDialog(
    errorMessage: String,
    onCloseErrorMessage: () -> Unit
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
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        text = {
            Text(
                text = errorMessage,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        onDismissRequest = { onCloseErrorMessage() },
        confirmButton = {
            TextButton(
                onClick = { onCloseErrorMessage() },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.primary
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
            onCloseErrorMessage = {}
        )
    }
}