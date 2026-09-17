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


class WorkoutLibraryFragment : Fragment(R.layout.fragment_workout_library) {







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

        // Create the recyclerView
        val recyclerView =
            view.findViewById<RecyclerView>(R.id.workoutRecyclerView)

        // Config the grid layout of the recyclerView
        recyclerView.layoutManager =
            GridLayoutManager(requireContext(), 2)

        // Lounch a corouting to fetch the data
        viewLifecycleOwner.lifecycleScope.launch {
            val workouts = workoutDao.getAllWorkouts()

            // Fill the adapter
            recyclerView.adapter =
                WorkoutAdapter(workouts) { position ->

                }
        }

        // Get createWorkoutButton
        val createWorkoutButton =
            view.findViewById<Button>(R.id.createWorkoutButton)

        // Implement createWorkoutButton logic
        createWorkoutButton.setOnClickListener {
            findNavController().navigate(
                R.id.action_workoutLibraryFragment_to_createWorkoutFragment
            )
        }


    }
}