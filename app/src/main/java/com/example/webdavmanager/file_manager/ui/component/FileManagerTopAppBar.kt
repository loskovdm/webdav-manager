package com.example.webdavmanager.file_manager.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.NoteAdd
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.CreateNewFolder
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
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
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.webdavmanager.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FileManagerTopAppBar(
    modifier: Modifier = Modifier,
    directoryName: String,
    onNavigateBack: () -> Unit,

    onUploadFileClick: () -> Unit,
    onCreateFolderClick: () -> Unit,

    isSelectionModeActive: Boolean,
    closeSelectionModeActive: () -> Unit,
    numberSelectedFiles: Int? = null,
    parentCheckBoxIsSelected: Boolean,
    onParentCheckBoxSelect: () -> Unit,
    onDeleteCheckedFile: () -> Unit,

    onSearchClick: () -> Unit,
    onCloseSearch: () -> Unit,
    isShowSearchBar: Boolean = false,
    currentSearchQuery: String = "",
    onSearchQueryChange: (String) -> Unit,

    currentSortOrder: SortOrder = SortOrder.NAME_ASC,
    onSortOptionSelect: (SortOrder) -> Unit,

    isShowPasteMenu: Boolean = false,
    onPasteClick: () -> Unit
) {
    var showAddFileMenu by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    var showSortMenu by remember { mutableStateOf(false) }

    if (isShowSearchBar) {
        // Replace SearchBar with TopAppBar containing TextField
        TopAppBar(
            modifier = modifier,
            title = {
                TextField(
                    value = currentSearchQuery,
                    onValueChange = onSearchQueryChange,
                    placeholder = {
                        Text(
                            "Search files...",
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    },
                    textStyle = MaterialTheme.typography.bodyLarge,
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.primary,
                        unfocusedContainerColor = MaterialTheme.colorScheme.primary,
                        focusedTextColor = MaterialTheme.colorScheme.onPrimary,
                        unfocusedTextColor = MaterialTheme.colorScheme.onPrimary,
                        focusedIndicatorColor = androidx.compose.ui.graphics.Color.Transparent,
                        unfocusedIndicatorColor = androidx.compose.ui.graphics.Color.Transparent,
                        cursorColor = MaterialTheme.colorScheme.onPrimary,
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester)
                )
            },
            navigationIcon = {
                IconButton(onClick = onCloseSearch) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            },
            actions = {
                if (currentSearchQuery.isNotEmpty()) {
                    IconButton(onClick = { onSearchQueryChange("") }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Clear search",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary,
                navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                actionIconContentColor = MaterialTheme.colorScheme.onPrimary
            )
        )

        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
    } else if (!isSelectionModeActive) {
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
                    onClick = { onSearchClick() }
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search"
                    )
                }
                Box {
                    IconButton(
                        onClick = { showSortMenu = true }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.Sort,
                            contentDescription = "Sort"
                        )
                    }
                    DropdownMenu(
                        expanded = showSortMenu,
                        onDismissRequest = { showSortMenu = false },
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        DropdownMenuItem(
                            text = { Text("Name") },
                            trailingIcon = {
                                if (currentSortOrder == SortOrder.NAME_ASC || currentSortOrder == SortOrder.NAME_DESC) {
                                    Icon(
                                        imageVector = if (currentSortOrder == SortOrder.NAME_ASC)
                                            Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                        contentDescription = "Sort by name"
                                    )
                                }
                            },
                            onClick = {
                                onSortOptionSelect(
                                    if (currentSortOrder == SortOrder.NAME_ASC)
                                        SortOrder.NAME_DESC else SortOrder.NAME_ASC
                                )
                                showSortMenu = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Size") },
                            trailingIcon = {
                                if (currentSortOrder == SortOrder.SIZE_ASC || currentSortOrder == SortOrder.SIZE_DESC) {
                                    Icon(
                                        imageVector = if (currentSortOrder == SortOrder.SIZE_ASC)
                                            Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                        contentDescription = "Sort by size"
                                    )
                                }
                            },
                            onClick = {
                                onSortOptionSelect(
                                    if (currentSortOrder == SortOrder.SIZE_ASC)
                                        SortOrder.SIZE_DESC else SortOrder.SIZE_ASC
                                )
                                showSortMenu = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Date") },
                            trailingIcon = {
                                if (currentSortOrder == SortOrder.DATE_ASC || currentSortOrder == SortOrder.DATE_DESC) {
                                    Icon(
                                        imageVector = if (currentSortOrder == SortOrder.DATE_DESC)
                                            Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                        contentDescription = "Sort by date"
                                    )
                                }
                            },
                            onClick = {
                                onSortOptionSelect(
                                    if (currentSortOrder == SortOrder.DATE_DESC)
                                        SortOrder.DATE_ASC else SortOrder.DATE_DESC
                                )
                                showSortMenu = false
                            }
                        )
                    }
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
                        if (isShowPasteMenu) {
                            DropdownMenuItem(
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.ContentPaste,
                                        contentDescription = "Paste file"
                                    )
                                },
                                text = { Text("Paste file") },
                                onClick = {
                                    onPasteClick()
                                    showAddFileMenu = false
                                }
                            )
                        }
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

enum class SortOrder {
    NAME_ASC, NAME_DESC,
    SIZE_ASC, SIZE_DESC,
    DATE_ASC, DATE_DESC
}