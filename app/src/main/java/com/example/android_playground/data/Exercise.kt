package com.example.android_playground.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Exercise(
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    val name : String,
    val muscleGroup : String,
    val videoUrl : String? = null
)

data class WorkoutExerciseDetails(
    val exerciseId: Int,
    val name: String,
    val muscleGroup: String,
    val exerciseOrder: Int,
    val sets: Int,
    val repetitions: Int
)