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

    @Query("""
    SELECT
        Exercise.id AS exerciseId,
        Exercise.name AS name,
        Exercise.muscleGroup AS muscleGroup,
        WorkoutExerciseCrossRef.exerciseOrder AS exerciseOrder,
        WorkoutExerciseCrossRef.sets AS sets,
        WorkoutExerciseCrossRef.repetitions AS repetitions
    FROM WorkoutExerciseCrossRef
    INNER JOIN Exercise
        ON WorkoutExerciseCrossRef.exerciseId = Exercise.id
    WHERE WorkoutExerciseCrossRef.workoutId = :workoutId
    ORDER BY WorkoutExerciseCrossRef.exerciseOrder ASC
""")
    suspend fun getExercisesForWorkout(workoutId: Int): List<WorkoutExerciseDetails>



    @Query("SELECT * FROM Exercise WHERE id = :exerciseId")
    suspend fun getExerciseById(exerciseId: Int): Exercise?


    @Query("DELETE FROM Exercise")
    suspend fun deleteAllExercises()
}
