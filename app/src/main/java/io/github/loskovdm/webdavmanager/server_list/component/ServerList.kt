package io.github.loskovdm.webdavmanager.server_list.component

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.loskovdm.webdavmanager.server_list.model.ServerItem
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.loskovdm.webdavmanager.core.ui.theme.WebdavManagerTheme

@Composable
fun ServerList(
    servers: List<ServerItem>,
    onDeleteServer: (Int) -> Unit,
    onNavigateToServerConfig: (Int) -> Unit,
    onNavigateToServerFileManager: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        items(servers) { server ->
            ServerItem(
                server = server,
                onServerClick = { onNavigateToServerFileManager(server.id) },
                onDeleteClick = { onDeleteServer(server.id) },
                onEditClick = { serverId ->
                    onNavigateToServerConfig(serverId)
                },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
    }
}

@Preview
@Composable
fun PreviewServerList() {
    WebdavManagerTheme {
        ServerList(
            servers =  listOf(
                ServerItem(id = 1, name = "Home Server", user = "admin"),
                ServerItem(id = 2, name = "Work WebDAV", user = "user123"),
                ServerItem(id = 3, name = "Cloud Storage", user = "clouduser")
            ),
            onDeleteServer = {},
            onNavigateToServerFileManager = {},
            onNavigateToServerConfig = {}
        )
    }
}