package com.example.dactylife.data.model

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
val today = LocalDate.now();
val fakeTasks = listOf<Task>(
    Task(1, "Drink water", TaskType.HABIT),
    Task(2, "Stretch", TaskType.HABIT),
    Task(3, "Buy groceries", TaskType.TODO),
    Task(4, "Change air filter", TaskType.EVENT, scheduledDate = today)
)