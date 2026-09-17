package com.example.android_playground.repository

import com.example.android_playground.data.Workout
import com.example.android_playground.data.WorkoutDao

class WorkoutRepository(
    private val workoutDao: WorkoutDao
) {

    suspend fun insertWorkout(workout: Workout) {
        workoutDao.insertWorkout(workout)
    }

    suspend fun deleteWorkout(workout: Workout) {
        workoutDao.deleteWorkout(workout)
    }

    suspend fun getAllWorkouts(): List<Workout> {
        return workoutDao.getAllWorkouts()
    }
}