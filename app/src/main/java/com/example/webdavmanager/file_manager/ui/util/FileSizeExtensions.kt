package com.example.webdavmanager.file_manager.ui.util

import kotlin.math.log10
import kotlin.math.pow

fun Long.formatAsFileSize(): String {
    if (this < 0) return "0 B"

    val units = arrayOf("B", "KB", "MB", "GB", "TB")
    val digitGroups = (log10(this.toDouble()) / log10(1024.0)).toInt()

    return "%.1f %s".format(
        this / 1024.0.pow(digitGroups.toDouble()),
        units[digitGroups.coerceAtMost(units.size - 1)]
    )
}