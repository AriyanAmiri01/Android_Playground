package com.example.android_playground.repository
// @ description : Handles workouts creation, insertion, deletion and query



import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_playground.data.Workout
import kotlinx.coroutines.launch

class WorkoutLibraryViewModel(
    private val repository: WorkoutRepository
) : ViewModel(){

    // @ Description: Create workout is used after the workout was inserted and now it is the time to add its exercises to its CrossRef table
    fun createWorkout(
        workout : Workout,
        exerciseIds : List<Int>,
        onComplete: () -> Unit
    ){
        viewModelScope.launch {
            repository.createWorkout(
                workout,
                exerciseIds
            )

            onComplete()
        }
    }


    fun insertWorkout(workout: Workout) {
        viewModelScope.launch {
            repository.insertWorkout(workout)
        }
    }

    fun deleteWorkout(
        workout: Workout,
        onComplete: () -> Unit  // A CALLBACK function for refreshing
    ) {
        viewModelScope.launch {
            repository.deleteWorkout(workout)
        }

        onComplete()
    }

    fun getAllWorkouts(): List<Workout> {
        // We will change this later when we make the database reactive.
        return emptyList()
    }
}