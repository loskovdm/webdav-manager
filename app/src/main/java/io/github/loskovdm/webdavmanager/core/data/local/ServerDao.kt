package io.github.loskovdm.webdavmanager.core.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ServerDao {
    @Insert
    suspend fun insertServer(server: ServerEntity)

    @Update
    suspend fun updateServer(server: ServerEntity)

    @Delete
    suspend fun deleteServer(server: ServerEntity)

    @Query("select * from serverentity")
    suspend fun getAll(): List<ServerEntity>

    @Query("delete from serverentity")
    suspend fun deleteAll()

    @Query("select * from serverentity where id = :id")
    suspend fun getServerById(id: Int): ServerEntity?
}