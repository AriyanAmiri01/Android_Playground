package com.example.android_playground.repository

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_playground.data.FirestoreWorkout
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FirestoreWorkoutViewModel(
    private val repository: FirestoreWorkoutRepository
) : ViewModel() {

    // Create an empty container for workouts  and make them as MutableStateFlow, MutableStateFlow mean only the owner can change them
    private val _workouts = MutableStateFlow<List<FirestoreWorkout>>(emptyList()) // For read and modify
    val workouts: StateFlow<List<FirestoreWorkout>> = _workouts // Read only

    // Same for error
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    // @ description: Creats a workout inside the firestore.
    fun createWorkout(workout: FirestoreWorkout) {
        viewModelScope.launch {
            try {
                _error.value = null

                repository.createWorkout(workout)

                // Refresh the list after creation
                getWorkouts()

            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    // @ description: return list of all workouts as kotlin objects
    fun getWorkouts() {
        viewModelScope.launch {
            try {
                _error.value = null

                _workouts.value = repository.getWorkouts()

            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    // @ description: updates a workout using its ID
    fun updateWorkout(
        workoutId: String,
        workout: FirestoreWorkout
    ) {
        viewModelScope.launch {
            try {
                _error.value = null

                repository.updateWorkout(workoutId, workout)

                // Refresh the list after updating
                getWorkouts()

            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    // @ description: Deletes a workout just using its ID
    fun deleteWorkout(workoutId: String) {
        viewModelScope.launch {
            try {
                _error.value = null

                repository.deleteWorkout(workoutId)

                // Refresh the list after deleting
                getWorkouts()

            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

}