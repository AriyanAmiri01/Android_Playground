package com.example.android_playground.repository


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


// @ description: It is the class for creating the createWorkoutViewModel using exerciseRepository
class Exercise_ViewModelFactory(
    private val repository: Exercise_Repository
) : ViewModelProvider.Factory  // @note: It follows the android specific interface for factories
{
    // @description: the function that creates the viewmodel for room communication
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(Exercise_ViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return Exercise_ViewModel(repository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}