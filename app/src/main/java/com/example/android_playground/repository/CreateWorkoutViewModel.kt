package com.example.android_playground.repository

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_playground.data.Exercise
import kotlinx.coroutines.launch

class CreateWorkoutViewModel(
    private val repository : ExerciseRepository
) : ViewModel(){
    private var exercises: List<Exercise> = emptyList()

    fun getAllExercises(
        onResult: (List<Exercise>) -> Unit
    ){
        // @ description It returns exercises if they exist otherwise it creates them then returns them from the database
        viewModelScope.launch {
            var exercises = repository.getAllExercises()

            if (exercises.isEmpty()) {

                val defaultExercises = listOf(
                    Exercise(
                        name = "Bench Press",
                        muscleGroup = "Chest"
                    ),
                    Exercise(
                        name = "Squat",
                        muscleGroup = "Legs"
                    ),
                    Exercise(
                        name = "Deadlift",
                        muscleGroup = "Back"
                    ),
                    Exercise(
                        name = "Pull Up",
                        muscleGroup = "Back"
                    ),
                    Exercise(
                        name = "Shoulder Press",
                        muscleGroup = "Shoulders"
                    )
                )

                repository.insertExercises(defaultExercises)

                exercises = repository.getAllExercises()
            }

            onResult(exercises)
        }
    }

}