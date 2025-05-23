package io.github.loskovdm.webdavmanager.feature.filemanager.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import io.github.loskovdm.webdavmanager.core.ui.theme.WebdavManagerTheme

@Composable
fun FileNameInputDialog(
    title: String,
    initialValue: String = "",
    confirmButtonText: String = "OK",
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var input by remember { mutableStateOf(initialValue) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            OutlinedTextField(
                value = input,
                onValueChange = { input = it },
                label = { Text("Name") },
                singleLine = true
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (input.isNotBlank()) {
                        onConfirm(input)
                        onDismiss
                    }
                }
            ) {
                Text(confirmButtonText)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Preview
@Composable
fun PreviewFileNameInputDialog() {
    WebdavManagerTheme {
        FileNameInputDialog(
            title = "Enter the name",
            onDismiss = {},
            onConfirm = {}
        )
    }
}