package com.example.webdavmanager.core.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ServerDao {
    @Insert
    suspend fun insertServer(server: Server)

    @Update
    suspend fun updateServer(server: Server)

    @Delete
    suspend fun deleteServer(server: Server)

    @Query("select * from server")
    suspend fun getAll(): List<Server>

    @Query("delete from server")
    suspend fun deleteAll()

    @Query("select * from server where id = :id")
    suspend fun getServerById(id: Int): Server?
}