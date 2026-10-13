package com.example.android_playground.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow


@Dao
interface WorkoutDao {

    // Workout OPERATIONS
    ///////////////////////////////////////////

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun Create_Workout(workout: Workout): Long

    @Query("SELECT * FROM workout")
    fun Read_Workouts(): Flow<List<Workout>>

    @Update
    suspend fun Update_Workout(workout: Workout)

    @Query("DELETE FROM Workout WHERE id = :workoutId")
    suspend fun Delete_Workout(workoutId: Int)

    // WorkoutExercise_CrossRep OPERATIONS
    ///////////////////////////////////////////
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun Create_WorkoutExercise_CrossRef(crossRef: WorkoutExerciseCrossRef)

    @Query("""
    SELECT * FROM WorkoutExerciseCrossRef
    WHERE workoutId = :workoutId
    ORDER BY exerciseOrder ASC
""")
    suspend fun Read_WorkoutExercise_CrossRefs(workoutId: Int): List<WorkoutExerciseCrossRef>

    @Query("DELETE FROM WorkoutExerciseCrossRef WHERE workoutId = :workoutId")
    suspend fun Delete_WorkoutExercise_CrossRefs(workoutId: Int)

    // Complex Operation
    ///////////////////////////////////////////

    @Transaction
    suspend fun Create_WorkoutWithCrossRef(workout: Workout, exerciseIds: List<Int>): Long {
        val workoutId = Create_Workout(workout).toInt()
        exerciseIds.forEachIndexed { index, exerciseId ->
            Create_WorkoutExercise_CrossRef(WorkoutExerciseCrossRef(workoutId = workoutId, exerciseId = exerciseId, exerciseOrder = index, sets = 3, repetitions = 10))
        }

        return workoutId.toLong()
    }

}




@Dao
interface ExerciseDao{

    // CRUD OPERATIONS
    ///////////////////////////////////////////

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun Create_Exercise(exercise: Exercise): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun Create_Exercises(exercises: List<Exercise>)

    @Query("SELECT * FROM Exercise WHERE id = :exerciseId")
    suspend fun Read_Exercise(exerciseId: Int): Exercise?

    @Query("SELECT * FROM Exercise")
    suspend fun Read_AllExercises() : List<Exercise>

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
    suspend fun Read_ExercisesForWorkout(workoutId: Int): List<WorkoutExerciseDetails>

    @Delete
    suspend fun Delete_Exercise( exercise: Exercise)

    @Query("DELETE FROM Exercise")
    suspend fun Delete_AllExercises()

}




@Dao
interface WorkoutSessionDao {

    // WorkoutSession
    ///////////////////////////////////////////

    @Insert
    suspend fun Create_WorkoutSession(session: WorkoutSession): Long

    @Query("""
    SELECT
        WorkoutSession.sessionId AS sessionId,
        WorkoutSession.workoutId AS workoutId,
        Workout.name AS workoutName,
        WorkoutSession.startedAt AS startedAt,
        WorkoutSession.completedAt AS completedAt
    FROM WorkoutSession
    INNER JOIN Workout
        ON WorkoutSession.workoutId = Workout.id
    WHERE WorkoutSession.completedAt IS NOT NULL
    ORDER BY WorkoutSession.completedAt DESC
    """)
    suspend fun Read_WorkoutSession_Completed(): List<WorkoutSessionDetails>

    @Query("""
        UPDATE WorkoutSession
        SET completedAt = :completedAt
        WHERE sessionId = :sessionId
        """)
    suspend fun Update_WorkoutSession_Finish(sessionId: Int, completedAt: Long)

    // WorkoutSets
    ///////////////////////////////////////////
    @Insert
    suspend fun Create_WorkoutSet(workoutSet : WorkoutSet)

    @Insert
    suspend fun Create_WorkoutSets(workoutSets: List<WorkoutSet>)

    @Query("""
    SELECT
        WorkoutSet.setId AS setId,
        WorkoutSet.sessionId AS sessionId,
        WorkoutSet.exerciseId AS exerciseId,
        Exercise.name AS exerciseName,
        WorkoutSet.setNumber AS setNumber,
        WorkoutSet.repetitions AS repetitions
    FROM WorkoutSet
    INNER JOIN Exercise
        ON WorkoutSet.exerciseId = Exercise.id
    WHERE WorkoutSet.sessionId = :sessionId
    ORDER BY WorkoutSet.exerciseId, WorkoutSet.setNumber
    """)
    suspend fun Read_WorkoutSet_WorkoutSetDetails(sessionId: Int): List<WorkoutSetDetails>

    @Query("""
        SELECT * FROM WorkoutSet
        WHERE sessionId = :sessionId
        ORDER BY exerciseId, setNumber
        """)
    suspend fun Read_WorkoutSets_ForSession(sessionId: Int): List<WorkoutSet>
}