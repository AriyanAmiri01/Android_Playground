package com.example.android_playground.data


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Workout(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val name : String,
    val duration: Int
)

@Entity(
    primaryKeys = ["workoutId", "exerciseId"]
)
data class WorkoutExerciseCrossRef(
    val workoutId: Int,
    val exerciseId: Int,

    // Position of the exercise in this workout
    val exerciseOrder : Int,

    // Workout-specific configuration
    val sets: Int,
    val repetitions: Int
)


@Entity
data class WorkoutSession(
    @PrimaryKey(autoGenerate = true)
    val sessionId: Int = 0,

    val workoutId: Int,
    val startedAt: Long,
    val completedAt: Long? = null
)


@Entity
data class WorkoutSet(
    @PrimaryKey(autoGenerate = true)
    val setId: Int = 0,

    val sessionId: Int,
    val exerciseId: Int,
    val setNumber: Int,
    val repetitions: Int
)


data class WorkoutSessionDetails(
    val sessionId: Int,
    val workoutId: Int,
    val workoutName: String,
    val startedAt: Long,
    val completedAt: Long
)


data class WorkoutSetDetails(
    val setId: Int,
    val sessionId: Int,
    val exerciseId: Int,
    val exerciseName: String,
    val setNumber: Int,
    val repetitions: Int
)