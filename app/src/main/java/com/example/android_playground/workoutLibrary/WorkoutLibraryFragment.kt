package com.example.android_playground.workoutLibrary

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android_playground.R
import com.example.android_playground.data.WorkoutDatabase
import kotlinx.coroutines.launch
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.example.android_playground.exercise.WorkoutAdapter
import androidx.lifecycle.ViewModelProvider
import com.example.android_playground.repository.WorkoutViewModel
import com.example.android_playground.repository.WorkoutViewModelFactory
import com.example.android_playground.repository.WorkoutRepository

// FIRESTORE STUFF
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle

class WorkoutLibraryFragment : Fragment(R.layout.fragment_workout_library) {

    // Room + Firestore
    private lateinit var viewModel: WorkoutViewModel



    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        // Notify the childrens?
        super.onViewCreated(view, savedInstanceState)

        // Get the database
        val database = WorkoutDatabase.getDatabase(requireContext())

        // ROOM COMMUNICATIONS
        val workoutRepository = WorkoutRepository(database.workoutDao())
        val workoutFactory = WorkoutViewModelFactory(workoutRepository)
        viewModel = ViewModelProvider(this, workoutFactory)[WorkoutViewModel::class.java]

        // Create the recyclerView
        val recyclerView = view.findViewById<RecyclerView>(R.id.workoutRecyclerView)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)


        // Observe workouts
        viewLifecycleOwner.lifecycleScope.launch {

            viewModel.syncWorkouts()

            viewLifecycleOwner.repeatOnLifecycle(
                Lifecycle.State.STARTED
            ) {

                viewModel.getAllWorkouts().collect { workouts ->

                    val adapter = WorkoutAdapter(
                        workouts = workouts,

                        onDeleteClick = { workout ->
                            viewModel.deleteWorkout(workout) {
                            }
                        },

                        onWorkoutClick = { workout ->

                            val bundle = Bundle().apply {
                                putInt(
                                    "workoutId",
                                    workout.id
                                )
                            }

                            findNavController().navigate(
                                R.id.action_workoutLibraryFragment_to_workoutDetailsFragment,
                                bundle
                            )
                        }
                    )

                    recyclerView.adapter = adapter
                }
            }
        }


        // Create workout button
        val createWorkoutButton =
            view.findViewById<Button>(
                R.id.createWorkoutButton
            )
        createWorkoutButton.setOnClickListener {
            findNavController().navigate(
                R.id.action_workoutLibraryFragment_to_createWorkoutFragment
            )
        }
    }



}