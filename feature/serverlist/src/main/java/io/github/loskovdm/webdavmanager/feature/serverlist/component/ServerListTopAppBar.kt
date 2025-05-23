package io.github.loskovdm.webdavmanager.feature.serverlist.component

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import io.github.loskovdm.webdavmanager.core.ui.R
import io.github.loskovdm.webdavmanager.core.ui.theme.WebdavManagerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServerListTopAppBar(
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.titleLarge
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary
        ),
        modifier = modifier
    )
}

@Preview
@Composable
fun PreviewServerListTopAppBar() {
    WebdavManagerTheme {
        ServerListTopAppBar()
    }
}