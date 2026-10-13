package com.example.android_playground.repository


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class Workout_ViewModelFactory(
    private val repository: Workout_Repository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {
        if (modelClass.isAssignableFrom(Workout_ViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return Workout_ViewModel(repository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}