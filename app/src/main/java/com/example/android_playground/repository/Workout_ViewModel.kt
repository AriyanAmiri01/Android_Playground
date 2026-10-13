package com.example.android_playground.repository
// @ description : Handles workouts creation, insertion, deletion and query



import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_playground.data.Workout
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.Flow

class Workout_ViewModel(private val repository: Workout_Repository) : ViewModel(){

    // Workout + WorkoutExerciseCrossRef Methods
    ///////////////////////////////////////////

    fun Create_Workout(workout : Workout, exerciseIds : List<Int>, onComplete: () -> Unit){
        viewModelScope.launch {
            repository.CREATE_Workout(workout = workout, exerciseIds = exerciseIds)
            onComplete()
        }
    }
    fun Read_AllWorkouts(): Flow<List<Workout>> {
        return repository.Room_Read_Workouts()
    }
    fun Delete_Workout(workout: Workout, onComplete: () -> Unit) {
        viewModelScope.launch {
            repository.DELETE_Workout(workout.id.toString())
            onComplete()
        }
    }
    suspend fun Sync_Workouts(){
        repository.SYNC_Workouts()
    }

}