package com.example.dactylife.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dactylife.data.repository.HabitCompletionRepository
import com.example.dactylife.data.repository.TaskRepository

class HistoryViewModelFactory(
    private val taskRepository: TaskRepository,
    private val habitRepository: HabitCompletionRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HistoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HistoryViewModel(taskRepository, habitRepository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}