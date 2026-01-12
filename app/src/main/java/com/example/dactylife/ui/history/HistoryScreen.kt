package com.example.dactylife.ui.history

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.dactylife.data.entity.TaskEntity
import com.example.dactylife.viewmodel.HistoryViewModel
import androidx.compose.foundation.lazy.items


@Composable
fun HistoryScreen(viewModel: HistoryViewModel) {

    val habits by viewModel.habits.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(habits, key = { it.id }) { habit ->
            HabitHistoryItem(
                habit = habit,
                viewModel = viewModel
            )
        }
    }
}

@Composable
private fun HabitHistoryItem(
    habit: TaskEntity,
    viewModel: HistoryViewModel
) {
    val completions by viewModel
        .habitCompletions(habit.id)
        .collectAsState(initial = emptySet())

    val streak = remember(completions) {
        viewModel.calculateStreak(completions)
    }

    Column(modifier = Modifier.padding(vertical = 12.dp)) {
        Text(
            text = habit.title,
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = "🔥 $streak day streak",
            color = MaterialTheme.colorScheme.primary
        )

        HabitHeatMap(completedDates = completions)
    }
}
