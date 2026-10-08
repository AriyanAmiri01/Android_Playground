package com.example.android_playground.workoutLibrary

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android_playground.R
import com.example.android_playground.data.WorkoutDatabase
import com.example.android_playground.exercise.WorkoutHistoryDetailsAdapter
import com.example.android_playground.repository.ActiveWorkoutViewModel
import com.example.android_playground.repository.ActiveWorkoutViewModelFactory
import com.example.android_playground.repository.WorkoutSessionRepository

class WorkoutHistoryDetailsFragment :
    Fragment(R.layout.sub_frag_workout_history_details) {

    private lateinit var viewModel:
            ActiveWorkoutViewModel

    private lateinit var adapter:
            WorkoutHistoryDetailsAdapter


    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(
            view,
            savedInstanceState
        )


        // 1. Get session information.
        val sessionId =
            arguments?.getInt("sessionId")
                ?: return

        val workoutName =
            arguments?.getString("workoutName")
                ?: "Workout"


        // 2. Set title.
        val title =
            view.findViewById<TextView>(
                R.id.historyDetailsTitle
            )

        title.text = workoutName


        // 3. Create adapter.
        adapter =
            WorkoutHistoryDetailsAdapter()


        // 4. Configure RecyclerView.
        val recyclerView =
            view.findViewById<RecyclerView>(
                R.id.historyDetailsRecyclerView
            )

        recyclerView.layoutManager =
            LinearLayoutManager(
                requireContext()
            )

        recyclerView.adapter =
            adapter


        // 5. Database.
        val database =
            WorkoutDatabase.getDatabase(
                requireContext()
            )


        // 6. Repository.
        val repository =
            WorkoutSessionRepository(
                database.workoutSessionDao()
            )


        // 7. ViewModel.
        val factory =
            ActiveWorkoutViewModelFactory(
                repository
            )

        viewModel =
            ViewModelProvider(
                this,
                factory
            )[ActiveWorkoutViewModel::class.java]


        // 8. Load performed sets.
        viewModel.getWorkoutSetDetails(
            sessionId
        ) { sets ->

            adapter.submitList(
                sets
            )
        }
    }
}