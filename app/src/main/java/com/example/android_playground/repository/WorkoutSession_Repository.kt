package com.example.android_playground.repository

import com.example.android_playground.data.WorkoutSession
import com.example.android_playground.data.WorkoutSessionDao
import com.example.android_playground.data.WorkoutSessionDetails
import com.example.android_playground.data.WorkoutSet
import com.example.android_playground.data.WorkoutSetDetails

class WorkoutSession_Repository(
    private val workoutSessionDao: WorkoutSessionDao
) {

    // WorkoutSession Methods
    ///////////////////////////////////////////

    suspend fun Create_WorkoutSession(workoutId: Int): Long {
        val session = WorkoutSession(workoutId = workoutId, startedAt = System.currentTimeMillis())
        return workoutSessionDao.Create_WorkoutSession(session)
    }
    suspend fun Read_WorkoutSession_Completed(): List<WorkoutSessionDetails> {
        return workoutSessionDao.Read_WorkoutSession_Completed()
    }
    suspend fun Update_WorkoutSession_Finish(sessionId: Int) {
        workoutSessionDao.Update_WorkoutSession_Finish(sessionId = sessionId, completedAt = System.currentTimeMillis())
    }

    // WorkoutSets Methods
    ///////////////////////////////////////////

    suspend fun Create_WorkoutSet(sessionId: Int, exerciseId: Int, setNumber: Int, repetitions: Int) {

        val workoutSet = WorkoutSet(sessionId = sessionId, exerciseId = exerciseId, setNumber = setNumber, repetitions = repetitions)

        workoutSessionDao.Create_WorkoutSet(workoutSet)
    }
    suspend fun Create_WorkoutSets(workoutSets: List<WorkoutSet>) {
        workoutSessionDao.Create_WorkoutSets(workoutSets)
    }
    suspend fun Read_WorkoutSets_ForSession(sessionId: Int): List<WorkoutSet> {
        return workoutSessionDao.Read_WorkoutSets_ForSession(sessionId)
    }
    suspend fun Read_WorkoutSet_WorkoutSetDetails(sessionId: Int): List<WorkoutSetDetails> {
        return workoutSessionDao.Read_WorkoutSet_WorkoutSetDetails(sessionId)
    }

    // Complex Methods
    ///////////////////////////////////////////

    suspend fun Update_WorkoutSession_FinishAndAddSets(sessionId: Int, workoutSets: List<WorkoutSet>) {
        workoutSessionDao.Create_WorkoutSets(workoutSets)
        workoutSessionDao.Update_WorkoutSession_Finish(sessionId = sessionId, completedAt = System.currentTimeMillis())
    }
}