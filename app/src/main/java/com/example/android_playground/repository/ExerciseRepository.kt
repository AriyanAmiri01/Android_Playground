package com.example.android_playground.repository

import com.example.android_playground.data.ExerciseDao
import com.example.android_playground.data.Exercise
import com.example.android_playground.data.WorkoutExerciseDetails

class ExerciseRepository (
    private val exerciseDao : ExerciseDao
){

    suspend fun insertExercise(exercise : Exercise){
        exerciseDao.insertExercise(exercise)
    }

    suspend fun insertExercises(exercises : List<Exercise>){
        exerciseDao.insertExercises(exercises)
    }

    suspend fun deleteExercise(exercise : Exercise){
        exerciseDao.deleteExercise(exercise)
    }

    suspend fun getAllExercises() : List<Exercise>{
        return exerciseDao.getAllExercises()
    }

    suspend fun roomGetExercisesForWorkout(workoutId : Int): List<WorkoutExerciseDetails>{
        return exerciseDao.getExercisesForWorkout(workoutId)
    }

    suspend fun READ_ExercisesForWorkout(workoutId : Int) : List<WorkoutExerciseDetails>{
        return roomGetExercisesForWorkout(workoutId)
    }

    suspend fun getExerciseById(exerciseId: Int): Exercise? {
        return exerciseDao.getExerciseById(exerciseId)
    }

    // Reseting
    suspend fun resetExercises() {
        exerciseDao.deleteAllExercises()
    }
}