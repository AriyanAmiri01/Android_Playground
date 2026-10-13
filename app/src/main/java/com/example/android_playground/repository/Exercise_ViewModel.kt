package com.example.android_playground.repository


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_playground.data.Exercise
import com.example.android_playground.data.WorkoutExerciseDetails
import kotlinx.coroutines.launch


class Exercise_ViewModel(private val repository : Exercise_Repository) : ViewModel(){

    private var exercises: List<Exercise> = emptyList()
    private var exerciseDetails: List<WorkoutExerciseDetails> = emptyList()


    fun Read_ExercisesForWorkout(workoutId : Int, onResult: (List<WorkoutExerciseDetails>) -> Unit){
        viewModelScope.launch{
            exerciseDetails = repository.Read_ExercisesForWorkout(workoutId)

            onResult(exerciseDetails)
        }
    }
    fun Read_Exercise(exerciseId: Int, onResult: (Exercise?) -> Unit) {
        viewModelScope.launch {
            val exercise = repository.Read_Exercise(exerciseId)
            onResult(exercise)
        }
    }
    fun Read_AllExercises(onResult: (List<Exercise>) -> Unit){
        viewModelScope.launch {
            var exercises = repository.Read_AllExercises()

            onResult(exercises)
        }
    }
    suspend fun Delete_Exercises(){
        repository.Delete_AllExercises()
    }

}