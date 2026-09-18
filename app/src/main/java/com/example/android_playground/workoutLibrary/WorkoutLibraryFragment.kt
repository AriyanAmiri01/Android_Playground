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


class WorkoutLibraryFragment : Fragment(R.layout.fragment_workout_library) {



    private lateinit var viewModel: WorkoutLibraryViewModel



    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        // Notify the childrens?
        super.onViewCreated(view, savedInstanceState)

        // Get the database
        val database =
            WorkoutDatabase.getDatabase(requireContext())

        // Get workoutDao
        val workoutDao =
            database.workoutDao()

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