package com.example.webdavmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.webdavmanager.core.ui.theme.WebdavManagerTheme
import com.example.webdavmanager.navigation.WebDavNavHost
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WebdavManagerTheme {
                val navController = rememberNavController()
                WebDavNavHost(navController = navController)
            }
        }
    }
}
