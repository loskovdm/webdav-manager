package io.github.loskovdm.webdavmanager.feature.filemanager.component

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import io.github.loskovdm.webdavmanager.feature.filemanager.model.FileItem
import io.github.loskovdm.webdavmanager.feature.filemanager.util.formatAsFileSize

@Composable
fun LargeFileWarningDialog(
    file: FileItem,
    fileSizeThreshold: Long = 10 * 1024 * 1024, // 10MB default
    onDismiss: () -> Unit,
    onOpenAnyway: () -> Unit,
    onSaveInDownloads: () -> Unit,
    onSaveInCustomDirectory: () -> Unit
) {
    if (file.size == null || file.size < fileSizeThreshold) {
        onOpenAnyway()
        return
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Large File Warning") },
        text = {
            Text("The selected file is large (${file.size.formatAsFileSize()}). " +
                    "Opening it may take time and consume data. Would you like to download it instead?")
        },
        confirmButton = { }, // Empty confirm button slot
        dismissButton = {
            Column {
                TextButton(onClick = onOpenAnyway) {
                    Text("Open Anyway")
                }
                TextButton(onClick = onSaveInDownloads) {
                    Text("Save to Downloads")
                }
                TextButton(onClick = onSaveInCustomDirectory) {
                    Text("Save to Custom Location")
                }
                TextButton(onClick = onDismiss) {
                    Text("Cancel")
                }
            }
        }
    )
}