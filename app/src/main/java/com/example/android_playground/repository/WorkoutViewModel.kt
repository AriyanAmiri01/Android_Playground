package com.example.android_playground.repository
// @ description : Handles workouts creation, insertion, deletion and query



import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_playground.data.Workout
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.Flow

class WorkoutViewModel(
    private val repository: WorkoutRepository
) : ViewModel(){

    // @ Description: Create workout is used after the workout was inserted and now it is the time to add its exercises to its CrossRef table
    fun createWorkout(workout : Workout, exerciseIds : List<Int>, onComplete: () -> Unit){
        viewModelScope.launch {
            repository.CREATE_Workout(workout = workout, exerciseIds = exerciseIds)

            onComplete()
        }
    }

    fun getAllWorkouts(): Flow<List<Workout>> {
        return repository.roomReadWorkouts()
    }



    fun deleteWorkout(workout: Workout, onComplete: () -> Unit) {
        viewModelScope.launch {
            repository.DELETE_Workout(workout.id.toString())
            onComplete()
        }
    }

    suspend fun syncWorkouts(){
        repository.SYNC_Workouts()
    }



}