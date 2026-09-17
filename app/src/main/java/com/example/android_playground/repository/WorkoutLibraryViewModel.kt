package com.example.android_playground.repository

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_playground.data.Workout
import kotlinx.coroutines.launch

class WorkoutLibraryViewModel(
    private val repository: WorkoutRepository
) : ViewModel(){
    fun insertWorkout(workout: Workout) {
        viewModelScope.launch {
            repository.insertWorkout(workout)
        }
    }

    fun deleteWorkout(workout: Workout) {
        viewModelScope.launch {
            repository.deleteWorkout(workout)
        }
    }

    fun getAllWorkouts(): List<Workout> {
        // We will change this later when we make the database reactive.
        return emptyList()
    }
}