package com.example.android_playground.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction // For atomic operations, so if anything fails we can roll back

@Dao
interface WorkoutDao{

    // WORKOUT OPERATIONS
    ///////////////////////////////////////////
    @Insert
    suspend fun insertWorkout(workout: Workout) : Long
    // This long is a weird template argument that makes the room compiler make the function return the generated primarly key.

    @Delete
    suspend fun deleteWorkout(workout : Workout)

    @Query("SELECT * FROM workout")
    suspend fun getAllWorkouts(): List<Workout>


    // EXERCISE OPERATIONS
    ////////////////////////////////////////////
    @Insert
    suspend fun insertExercise(exercise : Exercise)

    @Query("SELECT * FROM Exercise")
    suspend fun getAllExercises(): List<Exercise>


    // WORKOUT - CROSS RELATIONSHIP

    @Insert
    suspend fun insertWorkoutExerciseCrossRef(
        crossRef : WorkoutExerciseCrossRef
    )

    @Transaction
    suspend fun createWorkoutWithExercises(
        workout : Workout,
        exerciseIds: List<Int>
    ){
        val workoutId = insertWorkout(workout).toInt()

        exerciseIds.forEach{ exerciseId ->
            insertWorkoutExerciseCrossRef(
                WorkoutExerciseCrossRef(
                    workoutId = workoutId,
                    exerciseId = exerciseId
                )
            )
        }
    }

    // DELETION
    ///////////////////////////////////////////////
    @Delete
    suspend fun deleteExercise(exercise: Exercise)

    @Query("DELETE FROM WorkoutExerciseCrossRef WHERE workoutId = :workoutId")
    suspend fun deleteWorkoutExerciseCrossRefs(workoutId: Int)

    @Transaction
    suspend fun deleteWorkoutWithExercises(workout: Workout) {
        deleteWorkoutExerciseCrossRefs(workout.id)
        deleteWorkout(workout)
    }
}