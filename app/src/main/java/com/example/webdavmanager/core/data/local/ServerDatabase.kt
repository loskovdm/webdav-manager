package com.example.webdavmanager.core.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Server::class], version = 1)
abstract class ServerDatabase : RoomDatabase() {
    abstract fun serverDao(): ServerDao

    companion object {
        @Volatile
        private var INSTANCE: ServerDatabase? = null

        fun getInstance(context: Context): ServerDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context = context.applicationContext,
                    klass = ServerDatabase::class.java,
                    name = "server_database"
                )
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}