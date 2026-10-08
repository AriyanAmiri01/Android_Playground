package com.example.android_playground.repository

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ActiveWorkoutViewModelFactory(
    private val repository: WorkoutSessionRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (modelClass.isAssignableFrom(ActiveWorkoutViewModel::class.java)) {

            @Suppress("UNCHECKED_CAST")
            return ActiveWorkoutViewModel(repository) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class"
        )
    }
}