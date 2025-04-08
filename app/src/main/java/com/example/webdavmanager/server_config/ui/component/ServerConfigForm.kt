package com.example.webdavmanager.server_config.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
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
    validateName: (String) -> String?,
    validateUrl: (String) -> String?,
    validateUser: (String) -> String?,
    validatePassword: (String) -> String?,
    modifier: Modifier = Modifier
) {
    var nameFieldTouched by remember { mutableStateOf(false) }
    var urlFieldTouched by remember { mutableStateOf(false) }
    var userFieldTouched by remember { mutableStateOf(false) }
    var passwordFieldTouched by remember { mutableStateOf(false) }

    var passwordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = name,
            onValueChange = {
                if (!nameFieldTouched) nameFieldTouched = true
                onChangeName(it)
            },
            label = {
                Text(
                    text = stringResource(R.string.name)
                )
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
            isError = nameFieldTouched && validateName(name) != null,
            supportingText = {
                if (nameFieldTouched) {
                    validateName(name)?.let { errorMessage ->
                        Text(
                            text = errorMessage
                        )
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = url,
            onValueChange = {
                if (!urlFieldTouched) urlFieldTouched = true
                onChangeUrl(it)
            },
            label = {
                Text(
                    text = stringResource(R.string.url)
                )
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
            isError = urlFieldTouched && validateUrl(url) != null,
            supportingText = {
                if (urlFieldTouched) {
                    validateUrl(url)?.let { errorMessage ->
                        Text(
                            text = errorMessage
                        )
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(4.dp))

        OutlinedTextField(
            value = user,
            onValueChange = {
                if (!userFieldTouched) userFieldTouched = true
                onChangeUser(it)
            },
            label = {
                Text(
                    text = stringResource(R.string.user)
                )
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
            isError = userFieldTouched && validateUser(user) != null,
            supportingText = {
                if (userFieldTouched) {
                    validateUser(user)?.let { errorMessage ->
                        Text(
                            text = errorMessage
                        )
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(4.dp))

        OutlinedTextField(
            value = password,
            onValueChange = {
                if (!passwordFieldTouched) passwordFieldTouched = true
                onChangePassword(it)
            },
            label = {
                Text(
                    text = stringResource(R.string.password)
                )
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            visualTransformation = if (passwordVisible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            trailingIcon = {
                IconButton(
                    onClick = {
                        passwordVisible = !passwordVisible
                    }
                ) {
                    Icon(
                        imageVector = if (passwordVisible) {
                            Icons.Filled.Visibility
                        } else {
                            Icons.Filled.VisibilityOff
                        },
                        contentDescription = if (passwordVisible) {
                            stringResource(R.string.hide_password)
                        } else {
                            stringResource(R.string.show_password)
                        }
                    )
                }
            },
            isError = passwordFieldTouched && validatePassword(user) != null,
            supportingText = {
                if (passwordFieldTouched) {
                    validatePassword(user)?.let { errorMessage ->
                        Text(
                            text = errorMessage
                        )
                    }
                }
            },
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
            onChangePassword = {},
            validateName = {""},
            validateUrl = {""},
            validateUser = {""},
            validatePassword = {""}
        )
    }
}