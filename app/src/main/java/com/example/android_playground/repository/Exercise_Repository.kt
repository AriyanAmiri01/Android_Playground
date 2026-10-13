package com.example.android_playground.repository

import com.example.android_playground.data.ExerciseDao
import com.example.android_playground.data.Exercise
import com.example.android_playground.data.WorkoutExerciseDetails

class Exercise_Repository (
    private val exerciseDao : ExerciseDao
){

    /// ROOM
    //////////////////////////////////////////
    suspend fun Create_Exercise(exercise : Exercise){
        exerciseDao.Create_Exercise(exercise)
    }
    suspend fun Create_Exercises(exercises : List<Exercise>){
        exerciseDao.Create_Exercises(exercises)
    }
    suspend fun Read_Exercise(exerciseId: Int): Exercise?{
        return exerciseDao.Read_Exercise(exerciseId)
    }
    suspend fun Read_AllExercises() : List<Exercise>{
        return exerciseDao.Read_AllExercises()
    }
    suspend fun Read_ExercisesForWorkout(workoutId : Int): List<WorkoutExerciseDetails>{
        return exerciseDao.Read_ExercisesForWorkout(workoutId)
    }
    suspend fun Delete_Exercise(exercise : Exercise){
        exerciseDao.Delete_Exercise(exercise)
    }
    suspend fun Delete_AllExercises() {
        exerciseDao.Delete_AllExercises()
    }
}