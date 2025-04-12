package com.example.webdavmanager.file_manager.data.repository

import com.example.webdavmanager.file_manager.data.remote.WebDavFileDataSource
import javax.inject.Inject

class WebDavFileRepositoryImpl @Inject constructor(
    private val webDavFileDataSource: WebDavFileDataSource
) {
}