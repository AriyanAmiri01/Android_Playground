package com.example.android_playground.repository

import com.example.android_playground.data.WorkoutSession
import com.example.android_playground.data.WorkoutSessionDao
import com.example.android_playground.data.WorkoutSessionDetails
import com.example.android_playground.data.WorkoutSet
import com.example.android_playground.data.WorkoutSetDetails

class WorkoutSessionRepository(
    private val workoutSessionDao: WorkoutSessionDao
) {

    // START WORKOUT
    ///////////////////////////////////////////

    // Creates a new workout session and returns its generated ID.
    suspend fun startWorkout(
        workoutId: Int
    ): Long {

        val session = WorkoutSession(
            workoutId = workoutId,
            startedAt = System.currentTimeMillis()
        )

        return workoutSessionDao.insertWorkoutSession(session)
    }

    suspend fun insertWorkoutSets(
        workoutSets: List<WorkoutSet>
    ) {
        workoutSessionDao.insertWorkoutSets(
            workoutSets
        )
    }

    suspend fun completeWorkout(
        sessionId: Int,
        workoutSets: List<WorkoutSet>
    ) {

        workoutSessionDao.insertWorkoutSets(
            workoutSets
        )

        workoutSessionDao.finishWorkoutSession(
            sessionId = sessionId,
            completedAt = System.currentTimeMillis()
        )
    }



    suspend fun getCompletedWorkoutSessions():
            List<WorkoutSessionDetails> {

        return workoutSessionDao
            .getCompletedWorkoutSessions()
    }
    // RECORD SET
    ///////////////////////////////////////////

    // Saves one completed set during the workout.
    suspend fun insertWorkoutSet(
        sessionId: Int,
        exerciseId: Int,
        setNumber: Int,
        repetitions: Int
    ) {

        val workoutSet = WorkoutSet(
            sessionId = sessionId,
            exerciseId = exerciseId,
            setNumber = setNumber,
            repetitions = repetitions
        )

        workoutSessionDao.insertWorkoutSet(workoutSet)
    }


    // FINISH WORKOUT
    ///////////////////////////////////////////

    // Marks the workout session as completed.
    suspend fun finishWorkout(
        sessionId: Int
    ) {

        workoutSessionDao.finishWorkoutSession(
            sessionId = sessionId,
            completedAt = System.currentTimeMillis()
        )
    }


    // READ SESSION SETS
    ///////////////////////////////////////////

    // Returns all sets performed during a session.
    suspend fun getSetsForSession(
        sessionId: Int
    ): List<WorkoutSet> {

        return workoutSessionDao.getSetsForSession(sessionId)
    }

    suspend fun getWorkoutSetDetails(
        sessionId: Int
    ): List<WorkoutSetDetails> {

        return workoutSessionDao.getWorkoutSetDetails(
            sessionId
        )
    }
}