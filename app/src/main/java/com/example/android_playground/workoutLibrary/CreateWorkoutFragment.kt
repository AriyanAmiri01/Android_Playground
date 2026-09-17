package com.example.android_playground.workoutLibrary

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android_playground.R
import com.example.android_playground.data.Exercise
import com.example.android_playground.data.WorkoutDatabase
import com.example.android_playground.exercise.ExerciseSelectionAdapter
import com.example.android_playground.exercise.SelectedExerciseAdapter
import com.example.android_playground.repository.ExerciseRepository
import com.example.android_playground.repository.CreateWorkoutViewModel
import com.example.android_playground.repository.CreateWorkoutViewModelFactory

class CreateWorkoutFragment : Fragment(R.layout.fragment_create_workout) {

    // List of exercises that we will be quering
    private val selectedExercises = mutableListOf<Exercise>()


    //Use uninitilized selectedExercise adapter because we need it for definign itself
    private lateinit var selectedAdapter: SelectedExerciseAdapter

    // A placeholder for the view model that will be created by the factory
    private lateinit var viewModel: CreateWorkoutViewModel;

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

        // Create Exercise Repositories using its Duo
        val exerciseRepository = ExerciseRepository(
            database.exerciseDao())

        // Create The factory using the repository
        val factory = CreateWorkoutViewModelFactory(
            exerciseRepository)

        // Create the viewmodel from the factory
        viewModel = ViewModelProvider(this, factory)[CreateWorkoutViewModel::class.java]

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
        viewModel.getAllExercises { exercises ->

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

    }
}