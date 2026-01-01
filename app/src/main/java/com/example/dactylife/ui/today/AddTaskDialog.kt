package com.example.dactylife.ui.today

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.dactylife.data.model.TaskType

@Composable
fun AddTaskDialog(
    onDismiss: () -> Unit,
    onAddTask: (String, TaskType) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf(TaskType.TODO) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    if (title.isNotBlank()) {
                        onAddTask(title, selectedType)
                    }
                }
            ) {
                Text("Add")
            }
        },

        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },

        title = {
            Text("New Task")
        },

        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Task title") },
                    singleLine = true
                )
            }

            Spacer(Modifier.height(16.dp))

            TaskTypeSelector(
                selected = selectedType,
                onSelected = { selectedType = it }
            )
        }
    )
}