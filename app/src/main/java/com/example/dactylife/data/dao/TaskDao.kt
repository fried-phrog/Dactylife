package com.example.dactylife.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.dactylife.data.entity.TaskEntity
import com.example.dactylife.data.model.TaskType
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface TaskDao {

    @Query("SELECT * FROM tasks")
    fun getAllTasks(): Flow<List<TaskEntity>>

    @Insert
    suspend fun insertTask(task: TaskEntity)

    @Update
    suspend fun updateTask(task: TaskEntity)

    @Query("""
        SELECT * FROM tasks
        WHERE type = :type
        AND date = :date
        ORDER BY id ASC
    """)
    fun getEventsForDate(
        date: LocalDate,
        type: TaskType = TaskType.EVENT
    ): Flow<List<TaskEntity>>

    @Query("""
        SELECT * FROM tasks
        WHERE type = :type
        AND date BETWEEN :start AND :end
    """)
    suspend fun getEventsBetween(
        start: LocalDate,
        end: LocalDate,
        type: TaskType = TaskType.EVENT
    ): List<TaskEntity>

    @Query("""
        SELECT DISTINCT date FROM tasks
        WHERE type = :type
        AND date BETWEEN :start AND :end
    """
    )
    suspend fun getEventDatesBetween(
        start: LocalDate,
        end: LocalDate,
        type: TaskType = TaskType.EVENT
    ): List<LocalDate>

// for task seeding
    @Query("""
        SELECT COUNT(*) FROM tasks
        WHERE type = :type
    """)
    suspend fun countTasksOfType(
        type: TaskType
    ): Int
}