package com.example.webdavmanager.server_config.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.webdavmanager.R
import com.example.webdavmanager.core.ui.theme.WebdavManagerTheme
import com.example.webdavmanager.server_config.domain.model.ServerConfig

@Composable
fun ServerConfig(
    serverConfig: ServerConfig,
    isNewServer: Boolean,
    onSaveClick: (ServerConfig) -> Unit,
    onNavigateUp: () -> Unit,
) {
    var name by remember { mutableStateOf(serverConfig.name) }
    var url by remember { mutableStateOf(serverConfig.url) }
    var user by remember { mutableStateOf(serverConfig.user) }
    var password by remember { mutableStateOf(serverConfig.password) }

    Scaffold(
        topBar = {
            ServerConfigTopAppBar(
                isNewServer,
                onSaveClick = {
                    onSaveClick(
                        ServerConfig(
                            id = serverConfig.id,
                            name = name,
                            url = url,
                            user = user,
                            password = password
                        )
                    )
                },
                onNavigateUp = onNavigateUp
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it},
                label = {
                    Text(text = stringResource(R.string.name))
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = url,
                onValueChange = { url = it},
                label = {
                    Text(text = stringResource(R.string.url))
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = user,
                onValueChange = { user = it},
                label = {
                    Text(text = stringResource(R.string.user))
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it},
                label = {
                    Text(text = stringResource(R.string.password))
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                )
            )
        }
    }
}

@Preview
@Composable
fun PreviewServerConfig() {
    WebdavManagerTheme {
        ServerConfig(
            ServerConfig(
                id = 1,
                name = "Home server",
                url = "webdav://homeServer",
                user = "admin",
                password = "admin"
            ),
            isNewServer = false,
            onSaveClick = {},
            onNavigateUp = {}
        )
    }
}