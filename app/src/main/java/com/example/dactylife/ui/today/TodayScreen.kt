package com.example.dactylife.ui.today

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.dactylife.data.model.TaskType
import com.example.dactylife.ui.components.SectionTitle
import com.example.dactylife.viewmodel.TodayViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment


@Composable
fun TodayScreen(viewModel: TodayViewModel) {
    var showDialog by remember { mutableStateOf(false) }

    val tasks by viewModel.tasks.collectAsState()

    val habits = tasks.filter { it.type == TaskType.HABIT }
    val todos = tasks.filter { it.type == TaskType.TODO }
    val events = tasks.filter { it.type == TaskType.EVENT }


    Box (
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            item { SectionTitle("Habits") }
            items(habits) { task ->
                TaskRow(
                    task = task,
                    onToggle = { viewModel.toggleTask(task.id) }
                )
            }

            item { Spacer(Modifier.height(16.dp)) }
            item { SectionTitle("To-Do") }
            items(todos) { task ->
                TaskRow(
                    task = task,
                    onToggle = { viewModel.toggleTask(task.id) }
                )
            }

            item { Spacer(Modifier.height(16.dp)) }
            item { SectionTitle("Events") }
            items(events) { task ->
                TaskRow(
                    task = task,
                    onToggle = { viewModel.toggleTask(task.id) }
                )
            }
        }

        FloatingActionButton(
            onClick = { showDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add task")
        }
    }

    if (showDialog) {
        AddTaskDialog(
            onDismiss = { showDialog = false },
            onAddTask = { title, type ->
                viewModel.addTask(title, type)
                showDialog = false
            }
        )
    }
}
