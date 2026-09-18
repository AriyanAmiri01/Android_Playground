package com.example.android_playground.workoutLibrary

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android_playground.R
import com.example.android_playground.data.Exercise
import com.example.android_playground.data.Workout
import com.example.android_playground.data.WorkoutDatabase
import com.example.android_playground.exercise.ExerciseSelectionAdapter
import com.example.android_playground.exercise.SelectedExerciseAdapter
import com.example.android_playground.repository.ExerciseRepository
import com.example.android_playground.repository.CreateWorkoutViewModel
import com.example.android_playground.repository.CreateWorkoutViewModelFactory
import com.example.android_playground.repository.WorkoutLibraryViewModel
import com.example.android_playground.repository.WorkoutLibraryViewModelFactory
import com.example.android_playground.repository.WorkoutRepository
import android.widget.Button
import android.widget.EditText


class CreateWorkoutFragment : Fragment(R.layout.fragment_create_workout) {

    // List of exercises that we will be quering
    private val selectedExercises = mutableListOf<Exercise>()


    //Use uninitilized selectedExercise adapter because we need it for definign itself
    private lateinit var selectedAdapter: SelectedExerciseAdapter

    // A placeholder for the view model that will be created by the factory
    private lateinit var createWorkoutViewModel: CreateWorkoutViewModel;

    // For creating workouts
    private lateinit var workoutLibraryViewModel : WorkoutLibraryViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Notify all childrens
        super.onViewCreated(view, savedInstanceState)

        // Get the reference of the first recycler view
        val availableRecyclerView =
            view.findViewById<RecyclerView>(R.id.availableExercisesRecyclerView)

        // Get the reference of the second recycler view
        val selectedRecyclerView =
            view.findViewById<RecyclerView>(R.id.selectedExercisesRecyclerView)

        // Create the database with its duo
        val database = WorkoutDatabase.getDatabase(requireContext())

        // Create Exercise Repositories using their Duo
        val exerciseRepository = ExerciseRepository(
            database.exerciseDao())
        val workoutRepository = WorkoutRepository(
            database.workoutDao())

        // Create The factories using their repositories
        val createWorkoutViewModelFactory = CreateWorkoutViewModelFactory(
            exerciseRepository)
        val workoutLibraryViewModelFactory = WorkoutLibraryViewModelFactory(
            workoutRepository)


        // Create exercise and workout viewmodels from their factories
        createWorkoutViewModel = ViewModelProvider(
            this, createWorkoutViewModelFactory)[CreateWorkoutViewModel::class.java]
        workoutLibraryViewModel = ViewModelProvider(
            this, workoutLibraryViewModelFactory)[WorkoutLibraryViewModel::class.java]

        // Configure selected exercises RecyclerView
        selectedAdapter = SelectedExerciseAdapter(
            exercises = selectedExercises,
            onRemoveClick = { exercise ->

                selectedExercises.remove(exercise)

                selectedAdapter.notifyDataSetChanged()
            }
        )

        // Set its layout managaer
        selectedRecyclerView.layoutManager =
            LinearLayoutManager(requireContext())

        // Assign it to the recycler view
        selectedRecyclerView.adapter = selectedAdapter

        // Load exercises from Room
        createWorkoutViewModel.getAllExercises { exercises ->

            val availableAdapter = ExerciseSelectionAdapter(
                exercises = exercises,
                onAddClick = { exercise ->

                    if (!selectedExercises.contains(exercise)) {
                        selectedExercises.add(exercise)

                        selectedAdapter.notifyDataSetChanged()
                    }
                }
            )

            availableRecyclerView.adapter = availableAdapter
        }

        // Set its layout manager
        availableRecyclerView.layoutManager =
            LinearLayoutManager(requireContext())


        // @ description Finish creation with CreateButton

        // Get the name field
        val workoutNameEditText =
            view.findViewById<EditText>(R.id.workoutNameEditText)

        // Get the duration text field
        val workoutDurationEditText =
            view.findViewById<EditText>(R.id.workoutDurationEditText)

        // Get the create button reference
        val createWorkoutButton =
            view.findViewById<Button>(R.id.createWorkoutButton)

        // Add the eventListener
        createWorkoutButton.setOnClickListener{
            // Fill structure
            val workoutName = workoutNameEditText.text.toString().trim()
            if(workoutName.isEmpty()){
                workoutNameEditText.error = "Enter a workout name"
                return@setOnClickListener
            }

            val durationText = workoutDurationEditText.text.toString().trim()
            if (durationText.isEmpty()) {
                workoutDurationEditText.error = "Enter a duration"
                return@setOnClickListener
            }
            val duration = durationText.toInt()

            // Create the workout
            val workout = Workout(
                name = workoutName,
                duration = duration
            )

            // map its exercises
            val exerciseIds = selectedExercises.map { exercise ->
                exercise.id
            }

            // Let the viewmodel handle the rest while adding a callback function for reverse navigation
            workoutLibraryViewModel.createWorkout(
                workout = workout,
                exerciseIds = exerciseIds,
                onComplete = {
                    parentFragmentManager.popBackStack()
                }
            )
        }


    }
}