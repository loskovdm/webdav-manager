package io.github.loskovdm.webdavmanager.file_manager.ui.component

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DriveFileRenameOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.SaveAs
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.loskovdm.webdavmanager.core.ui.theme.WebdavManagerTheme
import io.github.loskovdm.webdavmanager.file_manager.ui.model.FileItem
import io.github.loskovdm.webdavmanager.file_manager.ui.util.formatAsFileSize
import io.github.loskovdm.webdavmanager.file_manager.ui.util.getFileIcon
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FileItem(
    modifier: Modifier = Modifier,
    file: FileItem,
    isSelectionModeActive: Boolean = false,
    isSelected: Boolean = false,
    onFileSelect: () -> Unit,
    onFileClick: () -> Unit,
    onFileLongClick: () -> Unit,
    onSaveInDownloadClick: () -> Unit,
    onSaveInCustomDirectoryClick: () -> Unit,
    onCopyClick: () -> Unit,
//    onMoveClick: () -> Unit,
    onRenameClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onInfoClick: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }

    Card(
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        shape = RectangleShape,
        modifier = modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = {
                    if (isSelectionModeActive) {
                        onFileSelect()
                    } else {
                        onFileClick()
                    }
                },
                onLongClick = {
                    if (!isSelectionModeActive) onFileLongClick()
                    Log.d("loadTest", "Run composable FileItem onLongClick")
                }
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = getFileIcon(file),
                contentDescription = null,
                modifier = Modifier
                    .size(52.dp)  // Increase this value to make icons larger
                    .padding(end = 16.dp),
                tint = MaterialTheme.colorScheme.primary,
            )
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = file.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    file.modifiedDate?.let { date ->
                        Text(
                            text = "${SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()).format(date)}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1
                        )
                    }
                    if (!file.isDirectory) {
                        Text(
                            text = file.size?.formatAsFileSize().toString(),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1
                        )
                    }
                }
            }

            if (isSelectionModeActive) {
                Checkbox(
                    checked = isSelected,
                    onCheckedChange = { onFileSelect() },
                    colors = CheckboxDefaults.colors(
                        checkedColor = MaterialTheme.colorScheme.primary
                    )
                )
            } else {
                Box {
                    IconButton(
                        onClick = { showMenu = true },
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = Color.Transparent,
                            contentColor = MaterialTheme.colorScheme.onSurface
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "More options"
                        )
                    }

                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false },
                        shape = RoundedCornerShape(16.dp)
                    ) {

                        if (!file.isDirectory) {
                            DropdownMenuItem(
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Save,
                                        contentDescription = null
                                    )
                                },
                                text = {
                                    Text(
                                        text = "Save in Downloads",
                                        style = MaterialTheme.typography.labelLarge
                                    )
                                },
                                onClick = {
                                    onSaveInDownloadClick()
                                    showMenu = false
                                }
                            )
                            DropdownMenuItem(
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.SaveAs,
                                        contentDescription = null
                                    )
                                },
                                text = {
                                    Text(
                                        text = "Save in ...",
                                        style = MaterialTheme.typography.labelLarge
                                    )
                                },
                                onClick = {
                                    onSaveInCustomDirectoryClick()
                                    showMenu = false
                                }
                            )
                            DropdownMenuItem(
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.ContentCopy,
                                        contentDescription = null
                                    )
                                },
                                text = {
                                    Text(
                                        text = "Copy",
                                        style = MaterialTheme.typography.labelLarge
                                    )
                                },
                                onClick = {
                                    onCopyClick()
                                    showMenu = false
                                }
                            )
//                            DropdownMenuItem(
//                                leadingIcon = {
//                                    Icon(
//                                        imageVector = Icons.AutoMirrored.Default.DriveFileMove,
//                                        contentDescription = null
//                                    )
//                                },
//                                text = {
//                                    Text(
//                                        text = "Move in ...",
//                                        style = MaterialTheme.typography.labelLarge
//                                    )
//                                },
//                                onClick = {
//                                    onMoveClick()
//                                    showMenu = false
//                                }
//                            )
                        }

                        DropdownMenuItem(
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.DriveFileRenameOutline,
                                    contentDescription = null
                                )
                            },
                            text = {
                                Text(
                                    text = "Rename",
                                    style = MaterialTheme.typography.labelLarge
                                )
                            },
                            onClick = {
                                onRenameClick()
                                showMenu = false
                            }
                        )
                        DropdownMenuItem(
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = null
                                )
                            },
                            text = {
                                Text(
                                    text = "Delete",
                                    style = MaterialTheme.typography.labelLarge
                                )
                            },
                            onClick = {
                                onDeleteClick()
                                showMenu = false
                            }
                        )
                        DropdownMenuItem(
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = null
                                )
                            },
                            text = {
                                Text(
                                    text = "Info",
                                    style = MaterialTheme.typography.labelLarge
                                )
                            },
                            onClick = {
                                onInfoClick()
                                showMenu = false
                            }
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewFileItem() {
    WebdavManagerTheme {
        FileItem(
            file = FileItem(
                name = "Test",
                isDirectory = false,
                mimeType = null,
                size = 1000,
                modifiedDate = null,
                creationDate = null,
                uri = ""
            ),
            onFileClick = {},
            onSaveInDownloadClick = {},
            onSaveInCustomDirectoryClick = {},
            onCopyClick = {},
//            onMoveClick = {},
            onRenameClick = {},
            onDeleteClick = {},
            isSelected = false,
            onFileSelect = {},
            onFileLongClick = {},
            isSelectionModeActive = false,
            onInfoClick = {}
        )
    }
}