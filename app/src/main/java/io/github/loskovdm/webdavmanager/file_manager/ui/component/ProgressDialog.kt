package io.github.loskovdm.webdavmanager.file_manager.ui.component

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.github.loskovdm.webdavmanager.R
import io.github.loskovdm.webdavmanager.file_manager.ui.OperationType

@Composable
fun ProgressDialog(
    operationType: OperationType,
    fileName: String,
    progress: Float,
    onCancelClick: () -> Unit
) {
    Log.e("loadTest", "Progress $progress")
    AlertDialog(
        onDismissRequest = {  },
        title = {
            Text(
                text = when (operationType) {
                    OperationType.UPLOAD -> "Uploading"
                    OperationType.DOWNLOAD -> "Downloading"
                    OperationType.OPEN -> "Open"
                    OperationType.DELETE -> "Delete"
                    OperationType.COPY -> "Copy"
                }
            )
        },
        text = {
            Column {
                Text(fileName)
                Spacer(modifier = Modifier.height(16.dp))
                LinearProgressIndicator(
                    progress = { progress / 100f },
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = "${(progress).toInt()}%",
                    modifier = Modifier.align(Alignment.End)
                )
            }
        },
        confirmButton = { },
        dismissButton = {
            TextButton(
                onClick = { onCancelClick() }
            ) { Text(stringResource(R.string.cancel)) }
        }
    )
}