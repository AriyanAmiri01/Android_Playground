package com.example.android_playground.repository


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


// @ description: It is the class for creating the createWorkoutViewModel using exerciseRepository
class ExerciseViewModelFactory(
    private val repository: ExerciseRepository
) : ViewModelProvider.Factory  // @note: It follows the android specific interface for factories
{
    // @description: the function that creates the viewmodel for room communication
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(ExerciseViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ExerciseViewModel(repository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}