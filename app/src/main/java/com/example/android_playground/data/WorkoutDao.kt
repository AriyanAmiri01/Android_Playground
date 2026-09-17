package com.example.android_playground.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface WorkoutDao{

    // WORKOUT OPERATIONS
    ///////////////////////////////////////////
    @Insert
    suspend fun insertWorkout(workout: Workout)

    @Delete
    suspend fun deleteWorkout(workout : Workout)

    @Query("SELECT * FROM workout")
    suspend fun getAllWorkouts(): List<Workout>


    // EXERCISE OPERATIONS
    ////////////////////////////////////////////
    @Insert
    suspend fun insertExercise(workout : Workout)

    @Query("SELECT * FROM Exercise")
    suspend fun getAllExercises(): List<Exercise>

    @Delete
    suspend fun deleteExercise(exercise: Exercise)
}