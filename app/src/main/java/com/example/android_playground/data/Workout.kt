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