package com.example.dactylife.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.dactylife.data.dao.TaskDao
import com.example.dactylife.data.entity.TaskEntity

@Database(
    entities = [TaskEntity::class],
    version = 1
)

@TypeConverters(Converters::class)
abstract class DactylifeDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}