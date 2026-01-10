package com.example.dactylife.ui.today

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.dactylife.data.model.TaskType
import java.time.LocalDate

@Composable
fun AddTaskDialog(
    onDismiss: () -> Unit,
    onAddTask: (String, TaskType, LocalDate?) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf(TaskType.HABIT) }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    if (title.isNotBlank()) {
                        onAddTask(
                            title,
                            selectedType,
                            if (selectedType == TaskType.EVENT) selectedDate else null
                        )
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
            Column (
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Task title") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                TaskTypeSelector(
                    selected = selectedType,
                    onSelected = { selectedType = it }
                )

                if (selectedType == TaskType.EVENT) {
                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Event date: $selectedDate",
                        modifier = Modifier.clickable {
                            selectedDate = selectedDate.plusDays(1)
                        }
                    )
                }
            }
        }
    )
}