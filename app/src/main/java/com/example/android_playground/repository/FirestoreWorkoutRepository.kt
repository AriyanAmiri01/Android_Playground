package com.example.android_playground.repository

import com.example.android_playground.data.FirestoreWorkout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await


class FirestoreWorkoutRepository() {
    // Firebase objects
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    // @ description: Used for navigation inside firestore
    private fun workoutsCollection() =
        firestore
            .collection("users")    // Goes to the users collection
            .document(auth.currentUser!!.uid)   //Selects the document of current user
            .collection("workouts") // Get the collection inside that document

    // @ description: Creats a workout inside the firestore.
    suspend fun createWorkout(workout: FirestoreWorkout): String {
        // Create a firebase document reference
        val document = workoutsCollection().document()

        // Assign a id to that workout(document)
        val workoutWithId = workout.copy(
            id = document.id
        )
        // Write the workout to it
        document.set(workoutWithId).await()

        return document.id
    }

    // @ description: return list of all workouts as kotlin objects
    suspend fun getWorkouts() : List<FirestoreWorkout>{
        // Gather all workouts inside workouts collection
        val snapshot = workoutsCollection()
            .get()
            .await()

        // Map firestore documents into kotlin object and return them
        return snapshot.documents.mapNotNull { document ->
            document.toObject(FirestoreWorkout::class.java)?.copy(
                id = document.id
            )
        }
    }

    // @ description: updates a workout using its ID
    suspend fun updateWorkout(
        workoutId: String,
        workout: FirestoreWorkout
    ) {
        val workoutWithId = workout.copy(
            id = workoutId
        )

        // Set a workout ased on its ID
        workoutsCollection()
            .document(workoutId)
            .set(workoutWithId)
            .await()
    }


    // @ description: Deletes a workout just using its ID
    suspend fun deleteWorkout(workoutId: String) {
        workoutsCollection()
            .document(workoutId)
            .delete()
            .await()
    }
}