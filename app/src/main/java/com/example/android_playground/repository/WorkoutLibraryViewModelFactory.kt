package com.example.android_playground.repository


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android_playground.repository.WorkoutRepository

class WorkoutLibraryViewModelFactory(
    private val repository: WorkoutRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {
        if (modelClass.isAssignableFrom(WorkoutLibraryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WorkoutLibraryViewModel(repository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}