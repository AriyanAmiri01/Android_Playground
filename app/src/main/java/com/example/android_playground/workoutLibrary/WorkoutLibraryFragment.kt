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
import com.example.android_playground.repository.WorkoutLibraryViewModel
import com.example.android_playground.repository.WorkoutLibraryViewModelFactory
import com.example.android_playground.repository.WorkoutRepository
import com.example.android_playground.data.Workout

// FIRESTORE STUFF
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.example.android_playground.repository.FirestoreWorkoutRepository
import com.example.android_playground.repository.FirestoreWorkoutViewModel
import com.example.android_playground.repository.FirestoreWorkoutViewModelFactory

class WorkoutLibraryFragment : Fragment(R.layout.fragment_workout_library) {



    private lateinit var viewModel: WorkoutLibraryViewModel

    private val firestoreWorkoutViewModel : FirestoreWorkoutViewModel by viewModels{
        FirestoreWorkoutViewModelFactory(
            FirestoreWorkoutRepository()
        )
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        // Notify the childrens?
        super.onViewCreated(view, savedInstanceState)

        firestoreWorkoutViewModel.getWorkouts()
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                firestoreWorkoutViewModel.workouts.collect { workouts ->

                    workouts.forEach { workout ->
                        println(
                            "Firestore workout: ${workout.id} - ${workout.name}"
                        )
                    }
                }
            }
        }

        // Get the database
        val database =
            WorkoutDatabase.getDatabase(requireContext())

        // ROOM COMMUNICATIONS
        val workoutRepository = WorkoutRepository(
            database.workoutDao())
        val workoutFactory = WorkoutLibraryViewModelFactory(
            workoutRepository)
        viewModel = ViewModelProvider(
            this, workoutFactory)[WorkoutLibraryViewModel::class.java]


        // Create the recyclerView
        val recyclerView =
            view.findViewById<RecyclerView>(R.id.workoutRecyclerView)

        // Config the grid layout of the recyclerView
        recyclerView.layoutManager =
            GridLayoutManager(requireContext(), 2)

        loadWorkouts()

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


    private fun loadWorkouts() {

        val database =
            WorkoutDatabase.getDatabase(requireContext())

        val workoutDao =
            database.workoutDao()

        viewLifecycleOwner.lifecycleScope.launch {

            val workouts =
                workoutDao.getAllWorkouts()

            val adapter = WorkoutAdapter(
                workouts = workouts,
                onDeleteClick = { workout ->

                    viewModel.deleteWorkout(workout) {
                        loadWorkouts()
                    }
                }
            )

            view?.findViewById<RecyclerView>(
                R.id.workoutRecyclerView
            )?.adapter = adapter
        }
    }

}