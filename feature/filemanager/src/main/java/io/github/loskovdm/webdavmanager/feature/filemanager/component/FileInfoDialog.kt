package io.github.loskovdm.webdavmanager.feature.filemanager.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.loskovdm.webdavmanager.core.ui.theme.WebdavManagerTheme
import io.github.loskovdm.webdavmanager.feature.filemanager.model.FileItem
import io.github.loskovdm.webdavmanager.feature.filemanager.util.formatAsFileSize
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun FileInfoDialog(
    file: FileItem,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = file.name,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        },
        text = {
            Column {
                InfoRow(label = "Type:", value = if (file.isDirectory) "Folder" else "File")

                if (!file.isDirectory) {
                    InfoRow(label = "Size:", value = file.size?.formatAsFileSize() ?: "0")
                }

                file.creationDate?.let {
                    InfoRow(
                        label = "Created:",
                        value = SimpleDateFormat("dd MMM yyyy, HH:mm:ss", Locale.getDefault()).format(it)
                    )
                }

                file.modifiedDate?.let {
                    InfoRow(
                        label = "Modified:",
                        value = SimpleDateFormat("dd MMM yyyy, HH:mm:ss", Locale.getDefault()).format(it)
                    )
                }

                InfoRow(label = "Path:", value = file.uri)
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.width(80.dp)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Preview
@Composable
fun PreviewFileInfoDialog() {
    WebdavManagerTheme {
        FileInfoDialog(
            file = FileItem(
                name = "testFile.txt",
                uri = "https://testServer/webdav/testFile.pdf",
                mimeType = "application/pdf",
                isDirectory = false,
                size = 123043L,
                creationDate = null,
                modifiedDate = null
            ),
            onDismiss = {}
        )
    }
}