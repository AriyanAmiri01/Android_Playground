package com.example.android_playground.data

data class FirestoreWorkout (
    val id : String = "",
    val name: String = "",
    val duration : Int = 0
)


data class FirestoreWorkoutExercise(
    val id: String = "",
    val workoutId: String = "",
    val exerciseId: String = "",
    val exerciseOrder: Int = 0,
    val sets: Int = 3,
    val repetitions: Int = 10
)