package com.example.android_playground.repository

import com.example.android_playground.data.ExerciseDao
import com.example.android_playground.data.Exercise

class ExerciseRepository (
    private val exerciseDao : ExerciseDao
){
    suspend fun insertExercise(exercise : Exercise){
        exerciseDao.deleteExercise(exercise)
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
}