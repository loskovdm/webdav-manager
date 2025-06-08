package io.github.loskovdm.webdavmanager.core.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ServerEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val url: String,
    val user: String,
    val password: String
)
