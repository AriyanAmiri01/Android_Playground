package com.example.android_playground.repository
// @ description: Handles exercise entities


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_playground.data.Exercise
import com.example.android_playground.data.WorkoutExerciseDetails
import kotlinx.coroutines.launch

class ExerciseViewModel(
    private val repository : ExerciseRepository
) : ViewModel(){
    private var exercises: List<Exercise> = emptyList()

    private var exerciseDetails: List<WorkoutExerciseDetails> = emptyList()


    fun loadExercisesForWorkout(workoutId : Int, onResult: (List<WorkoutExerciseDetails>) -> Unit){
        viewModelScope.launch{
            exerciseDetails = repository.READ_ExercisesForWorkout(workoutId)

            onResult(exerciseDetails)
        }
    }
    fun getExerciseById(exerciseId: Int, onResult: (Exercise?) -> Unit) {
        viewModelScope.launch {
            val exercise = repository.getExerciseById(exerciseId)
            onResult(exercise)
        }
    }



    fun getAllExercises(onResult: (List<Exercise>) -> Unit){
        // @ description It returns exercises if they exist otherwise it creates them then returns them from the database
        viewModelScope.launch {
            var exercises = repository.getAllExercises()

            if (exercises.isEmpty()) {

                val defaultExercises = listOf(
                    Exercise(
                        name = "Alternate Dumbbell Curls",
                        muscleGroup = "Biceps",
                        videoUrl = "gs://firbase-playground4.firebasestorage.app/exerciseGifs/alternateDumbleCurls.mp4"
                    ),

                    Exercise(
                        name = "Alternating Dumbbell Curl",
                        muscleGroup = "Biceps",
                        videoUrl = "gs://firbase-playground4.firebasestorage.app/exerciseGifs/alternatingDumbleCurl.mp4"
                    ),

                    Exercise(
                        name = "Barbell Chest Press",
                        muscleGroup = "Chest",
                        videoUrl = "gs://firbase-playground4.firebasestorage.app/exerciseGifs/barbellChestPress.mp4"
                    ),

                    Exercise(
                        name = "Barbell Prone Raise",
                        muscleGroup = "Shoulders",
                        videoUrl = "gs://firbase-playground4.firebasestorage.app/exerciseGifs/barbellProneRaise.mp4"
                    ),

                    Exercise(
                        name = "Barbell Reverse Curl",
                        muscleGroup = "Biceps",
                        videoUrl = "gs://firbase-playground4.firebasestorage.app/exerciseGifs/barbellReverseCurl.mp4"
                    ),

                    Exercise(
                        name = "Calf Raise Leg Press Machine",
                        muscleGroup = "Calves",
                        videoUrl = "gs://firbase-playground4.firebasestorage.app/exerciseGifs/calfRaiseLegPressMachine.mp4"
                    ),

                    Exercise(
                        name = "Dumbbell Skull Crusher",
                        muscleGroup = "Triceps",
                        videoUrl = "gs://firbase-playground4.firebasestorage.app/exerciseGifs/dumbellSkullCrush.mp4"
                    ),

                    Exercise(
                        name = "Dumbbell Prone Front Raise",
                        muscleGroup = "Shoulders",
                        videoUrl = "gs://firbase-playground4.firebasestorage.app/exerciseGifs/dumbleProneFrontRaise.mp4"
                    ),

                    Exercise(
                        name = "Dumbbell Tricep Extension",
                        muscleGroup = "Triceps",
                        videoUrl = "gs://firbase-playground4.firebasestorage.app/exerciseGifs/dumbleTriceptExtension.mp4"
                    ),

                    Exercise(
                        name = "Machine Back Fly",
                        muscleGroup = "Back",
                        videoUrl = "gs://firbase-playground4.firebasestorage.app/exerciseGifs/machineBackFly.mp4"
                    ),

                    Exercise(
                        name = "Seated Calf Machine",
                        muscleGroup = "Calves",
                        videoUrl = "gs://firbase-playground4.firebasestorage.app/exerciseGifs/seatedCalfMachine.mp4"
                    )
                )

                repository.insertExercises(defaultExercises)

                exercises = repository.getAllExercises()
            }

            onResult(exercises)
        }
    }



    // Reset stuffs
    suspend fun resetExercises(){
        repository.resetExercises()
    }
}