package com.example.android_playground.data

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Query
import androidx.room.Delete
import androidx.room.Insert


@Dao
interface ExerciseDao{
    @Query("SELECT * FROM Exercise")
    suspend fun getAllExercises() : List<Exercise>

    @Insert
    suspend fun insertExercise(exercise : Exercise)

    @Insert
    suspend fun insertExercises(exercises : List<Exercise>)

    @Delete
    suspend fun deleteExercise( exercise: Exercise)
}
