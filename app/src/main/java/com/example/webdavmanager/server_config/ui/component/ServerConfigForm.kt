package com.example.webdavmanager.server_config.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.webdavmanager.R
import com.example.webdavmanager.core.ui.theme.WebdavManagerTheme

@Composable
fun ServerConfigForm(
    name: String,
    url: String,
    user: String,
    password: String,
    onChangeName: (String) -> Unit,
    onChangeUrl: (String) -> Unit,
    onChangeUser: (String) -> Unit,
    onChangePassword: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = name,
            onValueChange = { onChangeName(it) },
            label = {
                Text(
                    text = stringResource(R.string.name)
                )
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = url,
            onValueChange = { onChangeUrl(it) },
            label = {
                Text(
                    text = stringResource(R.string.url)
                )
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = user,
            onValueChange = { onChangeUser(it) },
            label = {
                Text(
                    text = stringResource(R.string.user)
                )
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { onChangePassword(it) },
            label = {
                Text(
                    text = stringResource(R.string.password)
                )
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview
@Composable
fun PreviewServerConfigForm() {
    WebdavManagerTheme {
        ServerConfigForm(
            name = "Home server",
            url = "https://homeserver/webdav",
            user = "admin",
            password = "admin",
            onChangeName = {},
            onChangeUrl = {},
            onChangeUser = {},
            onChangePassword = {}
        )
    }
}