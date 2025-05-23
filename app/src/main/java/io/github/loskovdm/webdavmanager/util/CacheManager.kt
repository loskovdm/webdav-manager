package io.github.loskovdm.webdavmanager.util

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CacheManager @Inject constructor(@ApplicationContext private val context: Context) {
    suspend fun clearCache() {
        withContext(Dispatchers.IO) {
            context.cacheDir.listFiles()?.forEach { it.delete() }
        }
    }
}