package io.github.loskovdm.webdavmanager.core.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import io.github.loskovdm.webdavmanager.core.model.ServerModel

@Entity
data class ServerEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val url: String,
    val user: String,
    val password: String
)

fun ServerEntity.asExternalModel() = ServerModel(
    id = id,
    name = name,
    url = url,
    user = user,
    password = password
)

fun ServerModel.asServerEntity() = ServerEntity(
    id = id,
    name = name,
    url = url,
    user = user,
    password = password
)
