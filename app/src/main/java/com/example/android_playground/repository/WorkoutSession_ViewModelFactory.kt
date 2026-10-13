package com.example.android_playground.repository

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class WorkoutSession_ViewModelFactory(
    private val repository: WorkoutSession_Repository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (modelClass.isAssignableFrom(WorkoutSession_ViewModel::class.java)) {

            @Suppress("UNCHECKED_CAST")
            return WorkoutSession_ViewModel(repository) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class"
        )
    }
}