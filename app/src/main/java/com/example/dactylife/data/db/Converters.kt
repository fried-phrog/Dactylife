package com.example.dactylife.data.db

import androidx.room.TypeConverter
import com.example.dactylife.data.model.TaskType
import java.time.LocalDate

class Converters {
    @TypeConverter
    fun fromTaskType(type: TaskType): String = type.name

    @TypeConverter
    fun toTaskType(value: String): TaskType = TaskType.valueOf(value)

    @TypeConverter
    fun fromLocalDate(date: LocalDate?): String? = date?.toString()

    @TypeConverter
    fun toLocalDate(value: String?): LocalDate? = value?.let { LocalDate.parse(it) }
}