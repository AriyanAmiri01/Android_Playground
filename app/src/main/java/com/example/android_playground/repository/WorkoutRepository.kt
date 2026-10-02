package com.example.android_playground.repository


// LOCAL
import com.example.android_playground.data.Workout
import com.example.android_playground.data.WorkoutDao

// CLOUD
import com.example.android_playground.data.FirestoreWorkout
import com.example.android_playground.data.FirestoreWorkoutExercise
import com.example.android_playground.data.WorkoutExerciseCrossRef
import com.example.android_playground.data.WorkoutExerciseDetails
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import kotlin.text.get
import kotlinx.coroutines.flow.first

class WorkoutRepository(
    private val workoutDao: WorkoutDao
) {
    // ROOM + FIRESTORE
    suspend fun CREATE_Workout(workout: Workout, exerciseIds: List<Int>): Int{

        // Insert and get ID
        val generatedId = roomCreateWorkout(workout, exerciseIds).toInt()
        val workoutWithId = workout.copy(id = generatedId)

        // Firestore, create and insert
        val firestoreWorkout = FirestoreWorkout(id = generatedId.toString(), name = workoutWithId.name, duration = workoutWithId.duration)
        firestoreCreateWorkout(firestoreWorkout)


        // 3. Create the workout-exercise relationships in Firestore
        exerciseIds.forEachIndexed { index, exerciseId->
            firestoreCreateWorkoutExercise(
                workoutId = generatedId,
                exerciseId = exerciseId,
                exerciseOrder = index,
                sets = 3,
                repetitions = 10
            )
        }

        return generatedId
    }
    fun READ_Workouts() : Flow<List<Workout>>{
        return roomReadWorkouts()
    }
    suspend fun UPDATE_Workout(workoutId: String, workout:Workout){

        // 1. Update Room
        roomUpdateWorkout(workoutId.toInt(), workout)

        // 2. Update firestore
        val firestoreWorkout = FirestoreWorkout(id = workoutId, name = workout.name, duration = workout.duration)
        firestoreUpdateWorkout(workoutId = workoutId, workout = firestoreWorkout)

    }
    suspend fun DELETE_Workout(workoutId: String){
        // 1. Delete workout
        roomDeleteWorkoutById(workoutId.toInt())

        // 2.Delete workout-exercise
        firestoreDeleteWorkoutById(workoutId)


    }
    suspend fun SYNC_Workouts() {
        // 1. Get workouts and workoutExercise from Firestore
        val firestoreWorkouts = firestoreGetWorkouts()
        val firestoreWorkoutExercises = firestoreGetWorkoutExercises()


        // 2. Synchronize Room with Firebase
        roomSyncWorkouts(firestoreWorkouts)
        roomSyncWorkoutExercises(firestoreWorkoutExercises)
    }



    /// ROOM
    //////////////////////////////////////////

    suspend fun roomInsertWorkout(workout: Workout) : Long{
       return workoutDao.insertWorkout(workout) }
    suspend fun roomCreateWorkout(workout: Workout, exerciseIds: List<Int>): Long {
        return workoutDao.createWorkoutWithExercises(workout, exerciseIds) }
    fun roomReadWorkouts(): Flow<List<Workout>> {
        return workoutDao.getAllWorkouts() }
    suspend fun roomUpdateWorkout(workoutId: Int, workout: Workout){
        workoutDao.updateWorkout(workout);
    }
    suspend fun roomDeleteWorkoutById(workoutId : Int){
        workoutDao.deleteWorkoutExerciseCrossRefs(workoutId)
        workoutDao.deleteWorkoutById(workoutId)
    }
    suspend fun roomSyncWorkouts(firestoreWorkouts: List<FirestoreWorkout>) {
        // Get existing IDs in the firestore
        val firestoreIds = firestoreWorkouts
            .map{it.id.toInt()}
            .toSet()

        // Get current room workouts
        val roomWorkouts = roomReadWorkouts().first()

        // Delete room workouts that don't exist anymore
        roomWorkouts.forEach{ roomWorkout ->
            if(roomWorkout.id !in firestoreIds){
                roomDeleteWorkoutById(roomWorkout.id)
            }
        }

        // Insert workouts that are in firebase but not in room
        // Insert/update Firestore workouts in Room
        firestoreWorkouts.forEach { firestoreWorkout ->

            val workout = Workout(
                id = firestoreWorkout.id.toInt(),
                name = firestoreWorkout.name,
                duration = firestoreWorkout.duration
            )

            roomInsertWorkout(workout)
        }
    }
    suspend fun roomSyncWorkoutExercises(firestoreWorkoutExercises : List<FirestoreWorkoutExercise>){
        firestoreWorkoutExercises.forEach{ firestoreExercise ->


            if (firestoreExercise.workoutId.isBlank() || firestoreExercise.exerciseId.isBlank()) {
                println(
                    "SKIPPING INVALID WORKOUT EXERCISE: $firestoreExercise"
                )
                return@forEach
            }

            val crossRef = WorkoutExerciseCrossRef(
                workoutId = firestoreExercise.workoutId.toInt(),
                exerciseId = firestoreExercise.exerciseId.toInt(),
                exerciseOrder = firestoreExercise.exerciseOrder,
                sets = firestoreExercise.sets,
                repetitions = firestoreExercise.repetitions
            )

            workoutDao.insertWorkoutExerciseCrossRef(crossRef)
        }
    }


    /// FIRESTORE
    ////////////////////////////////////////////

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private fun firestoreWorkoutsCollection() = firestore
            .collection("users")
            .document(auth.currentUser!!.uid)
            .collection("workouts")
    private fun firestoreWorkoutExercisesCollection() =
        firestore
            .collection("users")
            .document(auth.currentUser!!.uid)
            .collection("workoutExercises")
    suspend fun firestoreCreateWorkout(workout: FirestoreWorkout): String {
        // @ description: Creats a workout inside the firestore.
        val documentId = workout.id

        firestoreWorkoutsCollection()
            .document(documentId)
            .set(workout)
            .await()



        return documentId
    }
    suspend fun firestoreCreateWorkoutExercise(workoutId: Int, exerciseId: Int, exerciseOrder: Int, sets: Int, repetitions: Int) {
        val relationshipId = "${workoutId}_${exerciseId}"

        val workoutExercise = FirestoreWorkoutExercise(
            id = relationshipId,
            workoutId = workoutId.toString(),
            exerciseId = exerciseId.toString(),
            exerciseOrder = exerciseOrder,
            sets = sets,
            repetitions = repetitions
        )

        println("CREATING WORKOUT EXERCISE: $workoutExercise")

        firestoreWorkoutExercisesCollection()
            .document(relationshipId)
            .set(workoutExercise)
            .await()
    }
    suspend fun firestoreGetWorkouts() : List<FirestoreWorkout>{
        // @ description: return list of all workouts as kotlin objects
        val snapshot = firestoreWorkoutsCollection()
            .get()
            .await()

        // Map firestore documents into kotlin object and return them
        return snapshot.documents.mapNotNull { document ->
            document.toObject(FirestoreWorkout::class.java)?.copy(
                id = document.id
            )
        }
    }
    suspend fun firestoreGetWorkoutExercises() :List<FirestoreWorkoutExercise>{
        val snapshot = firestoreWorkoutExercisesCollection()
            .get()
            .await()

        return snapshot.documents.mapNotNull { document ->
            document.toObject(FirestoreWorkoutExercise::class.java)?.copy(
                id = document.id
            )
        }
    }
    suspend fun firestoreUpdateWorkout(workoutId: String, workout: FirestoreWorkout) {
        // @ description: updates a workout using its ID
        val workoutWithId = workout.copy(
            id = workoutId
        )

        // Set a workout ased on its ID
        firestoreWorkoutsCollection()
            .document(workoutId)
            .set(workoutWithId)
            .await()
    }
    suspend fun firestoreDeleteWorkoutById(workoutId: String) {
        // 1. Delete workout
        firestoreWorkoutsCollection()
            .document(workoutId)
            .delete()
            .await()

        // 2. Delete workout-exercise crossover
        val snapshot = firestoreWorkoutExercisesCollection().whereEqualTo("workoutId", workoutId).get().await()
        snapshot.documents.forEach { document -> document.reference.delete().await() }
    }




}







