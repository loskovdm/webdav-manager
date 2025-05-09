package com.example.webdavmanager.file_manager.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.NoteAdd
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CreateNewFolder
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TriStateCheckbox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.webdavmanager.R
import com.example.webdavmanager.core.ui.theme.WebdavManagerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FileManagerTopAppBar(
    directoryName: String,
    isSelectionModeActive: Boolean,
    closeSelectionModeActive: () -> Unit,
    numberSelectedFiles: Int? = null,
    parentCheckBoxIsSelected: Boolean,
    onParentCheckBoxSelect: () -> Unit,
    onDeleteCheckedFile: () -> Unit,
    onNavigateBack: () -> Unit,
    onUploadFileClick: () -> Unit,
    onCreateFolderClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showAddFileMenu by remember { mutableStateOf(false) }
//    var showSortMenu by remember { mutableStateOf(false) }
//    var showSearchMenu by remember { mutableStateOf(false) }

    if (!isSelectionModeActive) {
        TopAppBar(
            modifier = modifier,
            title = {
                Text(
                    text = directoryName,
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
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
                    onClick = {  }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.Sort,
                        contentDescription = "Sort"
                    )
                }
                IconButton(
                    onClick = {  }
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search"
                    )
                }
                Box {
                    IconButton(
                        onClick = { showAddFileMenu = true }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add file"
                        )
                    }
                    DropdownMenu(
                        expanded = showAddFileMenu,
                        onDismissRequest = { showAddFileMenu = false },
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        DropdownMenuItem(
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Default.NoteAdd,
                                    contentDescription = "Upload file"
                                )
                            },
                            text = { Text("Upload file") },
                            onClick = {
                                onUploadFileClick()
                                showAddFileMenu = false
                            }
                        )
                        DropdownMenuItem(
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.CreateNewFolder,
                                    contentDescription = "Create folder"
                                )
                            },
                            text = { Text("Create folder") },
                            onClick = {
                                onCreateFolderClick()
                                showAddFileMenu = false
                            }
                        )
                    }
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary,
                actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
                navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
            )
        )
    } else {
        TopAppBar(
            modifier = modifier,
            title = {
                Text(
                    numberSelectedFiles.toString(),
                    style = MaterialTheme.typography.titleLarge
                )
            },
            navigationIcon = {
                IconButton(
                    onClick = { closeSelectionModeActive() }
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Clear selection"
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary,
                navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                actionIconContentColor = MaterialTheme.colorScheme.onPrimary
            ),
            actions = {
                IconButton(
                    onClick = { onDeleteCheckedFile() }
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null
                    )
                }
                Checkbox(
                    checked = parentCheckBoxIsSelected,
                    onCheckedChange = { onParentCheckBoxSelect() },
                    colors = CheckboxDefaults.colors(
                        checkedColor = MaterialTheme.colorScheme.onPrimary,
                        checkmarkColor = MaterialTheme.colorScheme.primary,
                        uncheckedColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
        )
    }


}

@Preview
@Composable
fun PreviewFileManagerTopAppBar() {
    WebdavManagerTheme {
        FileManagerTopAppBar(
            directoryName = "Test directory",
            onNavigateBack = {},
            onUploadFileClick = {},
            onCreateFolderClick = {},
            isSelectionModeActive = false,
            parentCheckBoxIsSelected = false,
            onParentCheckBoxSelect = {},
            closeSelectionModeActive = {},
            onDeleteCheckedFile = {}
        )
    }
}