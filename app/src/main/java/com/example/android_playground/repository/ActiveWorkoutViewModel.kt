package com.example.android_playground.repository

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_playground.data.WorkoutSessionDetails
import com.example.android_playground.data.WorkoutSet
import com.example.android_playground.data.WorkoutSetDetails
import kotlinx.coroutines.launch

class ActiveWorkoutViewModel(
    private val repository: WorkoutSessionRepository
) : ViewModel() {


    // START WORKOUT
    ///////////////////////////////////////////

    fun startWorkout(
        workoutId: Int,
        onStarted: (Int) -> Unit
    ) {
        viewModelScope.launch {

            val sessionId = repository.startWorkout(workoutId)

            // Room returns the generated ID as Long.
            // Our sessionId is Int, so convert it here.
            onStarted(sessionId.toInt())
        }
    }


    // RECORD SET
    ///////////////////////////////////////////

    fun insertWorkoutSet(
        sessionId: Int,
        exerciseId: Int,
        setNumber: Int,
        repetitions: Int
    ) {
        viewModelScope.launch {

            repository.insertWorkoutSet(
                sessionId = sessionId,
                exerciseId = exerciseId,
                setNumber = setNumber,
                repetitions = repetitions
            )
        }
    }


    // FINISH WORKOUT
    ///////////////////////////////////////////

    fun finishWorkout(
        sessionId: Int,
        onComplete: () -> Unit
    ) {
        viewModelScope.launch {

            repository.finishWorkout(sessionId)

            onComplete()
        }
    }


    // READ SESSION SETS
    ///////////////////////////////////////////

    fun getSetsForSession(
        sessionId: Int,
        onResult: (List<WorkoutSet>) -> Unit
    ) {
        viewModelScope.launch {

            val sets = repository.getSetsForSession(sessionId)

            onResult(sets)
        }
    }



    fun completeWorkout(
        sessionId: Int,
        workoutSets: List<WorkoutSet>,
        onComplete: () -> Unit
    ) {

        viewModelScope.launch {

            repository.completeWorkout(
                sessionId = sessionId,
                workoutSets = workoutSets
            )

            onComplete()
        }
    }


    fun getCompletedWorkoutSessions(
        onResult: (List<WorkoutSessionDetails>) -> Unit
    ) {
        viewModelScope.launch {

            val sessions =
                repository.getCompletedWorkoutSessions()

            onResult(sessions)
        }
    }

    fun getWorkoutSetDetails(
        sessionId: Int,
        onResult: (List<WorkoutSetDetails>) -> Unit
    ) {
        viewModelScope.launch {

            val sets =
                repository.getWorkoutSetDetails(
                    sessionId
                )

            onResult(sets)
        }
    }
}