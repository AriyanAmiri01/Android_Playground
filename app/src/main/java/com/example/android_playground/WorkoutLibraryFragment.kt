package com.example.android_playground

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
class WorkoutLibraryFragment : Fragment(R.layout.fragment_workout_library) {
    // Define a test list of workouts
    private val workouts = mutableListOf(
        Workout("Push Day", "Chest, shoulders and triceps"),
        Workout("Pull Day", "Back and biceps"),
        Workout("Leg Day", "Quads, hamstrings and calves")
    )

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)


        val recyclerView =
            view.findViewById<RecyclerView>(R.id.workoutRecyclerView)



        recyclerView.layoutManager =
            GridLayoutManager(requireContext(), 2)

        recyclerView.adapter =
            WorkoutAdapter(workouts)


    }




}