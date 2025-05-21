package io.github.loskovdm.webdavmanager.server_list.component

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.loskovdm.webdavmanager.R
import io.github.loskovdm.webdavmanager.core.ui.theme.WebdavManagerTheme

@Composable
fun ServerListFloatingActionButton(
    onClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    FloatingActionButton(
        onClick = { onClick(0) },
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = stringResource(R.string.add_server)
        )
    }
}

@Preview
@Composable
fun PreviewServerListFloatingActionButton() {
    WebdavManagerTheme {
        ServerListFloatingActionButton(onClick = {})
    }
}