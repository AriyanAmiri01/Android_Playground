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
        // @ note: It deletes workouts with their exercises
        workoutDao.deleteWorkoutWithExercises(workout)
    }

    suspend fun getAllWorkouts(): List<Workout> {
        return workoutDao.getAllWorkouts()
    }

    // @ description: Gets a workout and a list of exercises' ID and creates a new workout with its cross table for associated exercises
    suspend fun createWorkout(
        workout : Workout,
        exerciseIds: List<Int>
    ){
        workoutDao.createWorkoutWithExercises(
            workout,
            exerciseIds
        )
    }


}