package com.example.android_playground.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface WorkoutSessionDao {

    // START WORKOUT
    ///////////////////////////////////////////

    // Creates a new workout session.
    // Returns the automatically generated sessionId.
    @Insert
    suspend fun insertWorkoutSession(
        session: WorkoutSession
    ): Long

    @Insert
    suspend fun insertWorkoutSets(
        workoutSets: List<WorkoutSet>
    )

    // RECORD SET
    ///////////////////////////////////////////

    // Saves one completed set for an exercise.
    @Insert
    suspend fun insertWorkoutSet(
        workoutSet: WorkoutSet
    )


    // FINISH WORKOUT
    ///////////////////////////////////////////

    // Sets the time at which the workout was completed.
    @Query(
        """
        UPDATE WorkoutSession
        SET completedAt = :completedAt
        WHERE sessionId = :sessionId
        """
    )
    suspend fun finishWorkoutSession(
        sessionId: Int,
        completedAt: Long
    )


    // READ SESSION SETS
    ///////////////////////////////////////////

    // Gets all sets performed during a specific workout session.
    @Query(
        """
        SELECT * FROM WorkoutSet
        WHERE sessionId = :sessionId
        ORDER BY exerciseId, setNumber
        """
    )
    suspend fun getSetsForSession(
        sessionId: Int
    ): List<WorkoutSet>


    @Query(
        """
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
    """
    )
    suspend fun getCompletedWorkoutSessions():
            List<WorkoutSessionDetails>



    @Query(
        """
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
    """
    )
    suspend fun getWorkoutSetDetails(
        sessionId: Int
    ): List<WorkoutSetDetails>
}