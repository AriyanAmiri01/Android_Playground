package com.example.android_playground.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction // For atomic operations, so if anything fails we can roll back
import androidx.room.Update
import kotlinx.coroutines.flow.Flow


@Dao
interface WorkoutDao{

    // WORKOUT OPERATIONS
    ///////////////////////////////////////////
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkout(workout: Workout) : Long

    @Transaction
    suspend fun createWorkoutWithExercises(workout: Workout, exerciseIds: List<Int>): Long {

        val workoutId = insertWorkout(workout).toInt()

        exerciseIds.forEachIndexed { index, exerciseId ->

            insertWorkoutExerciseCrossRef(
                WorkoutExerciseCrossRef(
                    workoutId = workoutId,
                    exerciseId = exerciseId,
                    exerciseOrder = index,
                    sets = 3,
                    repetitions = 10
                )
            )
        }

        return workoutId.toLong()
    }

    @Query("SELECT * FROM workout")
    fun getAllWorkouts(): Flow<List<Workout>>

    @Update
    suspend fun updateWorkout(workout: Workout)

    @Delete
    suspend fun deleteWorkout(workout : Workout)

    @Query("DELETE FROM Workout WHERE id = :workoutId")
    suspend fun deleteWorkoutById(workoutId : Int)

    @Transaction
    suspend fun deleteWorkoutWithExercises(workout: Workout) {
        deleteWorkoutExerciseCrossRefs(workout.id)
        deleteWorkout(workout)
    }



    // EXERCISE OPERATIONS
    ////////////////////////////////////////////
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExercise(exercise: Exercise): Long

    @Query("SELECT * FROM Exercise")
    suspend fun getAllExercises(): List<Exercise>



    @Delete
    suspend fun deleteExercise(exercise: Exercise)

    // WORKOUT - CROSS RELATIONSHIP
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkoutExerciseCrossRef (
        crossRef : WorkoutExerciseCrossRef
    )

    @Query("""
        SELECT * FROM WorkoutExerciseCrossRef
        WHERE workoutId = :workoutId
        ORDER BY exerciseOrder ASC
    """)
    suspend fun getWorkoutExerciseCrossRefs(workoutId : Int): List<WorkoutExerciseCrossRef>

    @Query("DELETE FROM WorkoutExerciseCrossRef WHERE workoutId = :workoutId")
    suspend fun deleteWorkoutExerciseCrossRefs(workoutId :Int)





}