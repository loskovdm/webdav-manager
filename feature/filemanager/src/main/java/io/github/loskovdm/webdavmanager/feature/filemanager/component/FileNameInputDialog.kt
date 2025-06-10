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
    // Track invalid characters state
    var hasInvalidChars by remember { mutableStateOf(false) }
    // Define invalid characters for file/directory names
    val invalidChars = listOf('/', '\\', ':', '*', '?', '"', '<', '>', '|', '#', '$')

    // Validate input whenever it changes
    val validateInput: (String) -> Boolean = { text ->
        text.any { it in invalidChars }
    }

    // Check initial value
    hasInvalidChars = validateInput(input)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            OutlinedTextField(
                value = input,
                onValueChange = {
                    input = it
                    hasInvalidChars = validateInput(it)
                },
                label = { Text("Name") },
                singleLine = true,
                isError = hasInvalidChars,
                supportingText = {
                    if (hasInvalidChars) {
                        Text(
                            "Invalid characters: / \\ : * ? \" < > |",
                            color = androidx.compose.ui.graphics.Color.Red
                        )
                    }
                }
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (input.isNotBlank() && !hasInvalidChars) {
                        onConfirm(input)
                        onDismiss()
                    }
                },
                enabled = input.isNotBlank() && !hasInvalidChars
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