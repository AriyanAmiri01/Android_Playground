package com.example.android_playground.repository


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android_playground.repository.ExerciseRepository


// @ description: It is the class for creating the createWorkoutViewModel using exerciseRepository
class CreateWorkoutViewModelFactory(
    private val repository: ExerciseRepository
) : ViewModelProvider.Factory  // @note: It follows the android specific interface for factories
{

    // @description: the function that creates the viewmodel for room communication
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(CreateWorkoutViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CreateWorkoutViewModel(repository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}