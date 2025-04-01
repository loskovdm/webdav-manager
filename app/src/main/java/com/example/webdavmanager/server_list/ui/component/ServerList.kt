package com.example.webdavmanager.server_list.ui.component

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.webdavmanager.server_list.domain.model.ServerItem
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.webdavmanager.core.ui.theme.WebdavManagerTheme

@Composable
fun ServerList(
    servers: List<ServerItem>,
    onDeleteServer: (Int) -> Unit = {},
    onNavigateToServerFileManager: (Int) -> Unit = {},
    onNavigateToEditServer: (Int) -> Unit = {},
    onNavigateToAddServer: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            ServerListTopAppBar()
        },
        floatingActionButton = {
            ServerListFloatingActionButton(onClick = { onNavigateToAddServer })
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            items(servers) { server ->
                ServerItem(
                    server = server,
                    onServerClick = { onNavigateToServerFileManager(server.id) },
                    onDeleteClick = { onDeleteServer(server.id) },
                    onEditClick = { onNavigateToEditServer(server.id) },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewServerList() {
    WebdavManagerTheme {
        ServerList(listOf(
            ServerItem(id = 1, name = "Home Server", user = "admin"),
            ServerItem(id = 2, name = "Work WebDAV", user = "user123"),
            ServerItem(id = 3, name = "Cloud Storage", user = "clouduser")
        ))
    }
}