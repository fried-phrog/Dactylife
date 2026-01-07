package com.example.dactylife.data.db

import androidx.room.TypeConverter
import com.example.dactylife.data.model.TaskType

class Converters {
    @TypeConverter
    fun fromTaskType(type: TaskType): String = type.name

    @TypeConverter
    fun toTaskType(value: String): TaskType = TaskType.valueOf(value)
}