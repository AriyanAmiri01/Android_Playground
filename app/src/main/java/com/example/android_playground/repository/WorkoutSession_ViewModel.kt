package com.example.android_playground.repository

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_playground.data.WorkoutSessionDetails
import com.example.android_playground.data.WorkoutSet
import com.example.android_playground.data.WorkoutSetDetails
import kotlinx.coroutines.launch

class WorkoutSession_ViewModel(
    private val repository: WorkoutSession_Repository
) : ViewModel() {

    // WorkoutSession Methods
    ///////////////////////////////////////////

    fun Create_WorkoutSession(workoutId: Int, onStarted: (Int) -> Unit) {
        viewModelScope.launch {
            val sessionId = repository.Create_WorkoutSession(workoutId)
            onStarted(sessionId.toInt())
        }
    }
    fun Read_WorkoutSession_Completed(onResult: (List<WorkoutSessionDetails>) -> Unit) {
        viewModelScope.launch {
            val sessions = repository.Read_WorkoutSession_Completed()
            onResult(sessions)
        }
    }
    fun Update_WorkoutSession_Finish(sessionId: Int, onComplete: () -> Unit) {
        viewModelScope.launch {

            repository.Update_WorkoutSession_Finish(sessionId)

            onComplete()
        }
    }

    // WorkoutSets Methods
    ///////////////////////////////////////////

    fun Create_WorkoutSet(sessionId: Int, exerciseId: Int, setNumber: Int, repetitions: Int) {
        viewModelScope.launch {
            repository.Create_WorkoutSet(sessionId = sessionId, exerciseId = exerciseId, setNumber = setNumber, repetitions = repetitions)
        }
    }
    fun Read_WorkoutSet_WorkoutSetDetails(sessionId: Int, onResult: (List<WorkoutSetDetails>) -> Unit) {
        viewModelScope.launch {
            val sets = repository.Read_WorkoutSet_WorkoutSetDetails(sessionId)
            onResult(sets)
        }
    }
    fun Read_WorkoutSets_ForSession(sessionId: Int, onResult: (List<WorkoutSet>) -> Unit) {
        viewModelScope.launch {
            val sets = repository.Read_WorkoutSets_ForSession(sessionId)
            onResult(sets)
        }
    }

    // Complex Methods
    ///////////////////////////////////////////

    fun Update_WorkoutSession_FinishAndAddSets(sessionId: Int, workoutSets: List<WorkoutSet>, onComplete: () -> Unit) {
        viewModelScope.launch {
            repository.Update_WorkoutSession_FinishAndAddSets(sessionId = sessionId, workoutSets = workoutSets)
            onComplete()
        }
    }
}