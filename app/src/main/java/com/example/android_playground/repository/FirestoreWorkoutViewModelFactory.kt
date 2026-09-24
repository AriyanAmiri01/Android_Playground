package com.example.android_playground.repository

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class FirestoreWorkoutViewModelFactory(
    private val repository: FirestoreWorkoutRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (modelClass.isAssignableFrom(FirestoreWorkoutViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FirestoreWorkoutViewModel(repository) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class: ${modelClass.name}"
        )
    }
}