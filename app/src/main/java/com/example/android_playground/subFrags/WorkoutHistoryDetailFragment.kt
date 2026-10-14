package com.example.android_playground.subFrags

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android_playground.R
import com.example.android_playground.data.WorkoutDatabase
import com.example.android_playground.adapters.WorkoutHistoryDetailsAdapter
import com.example.android_playground.repository.WorkoutSession_ViewModel
import com.example.android_playground.repository.WorkoutSession_ViewModelFactory
import com.example.android_playground.repository.WorkoutSession_Repository


class WorkoutHistoryDetailsFragment : Fragment(R.layout.x05_sub_frag_workout_history_details) {

    // Variables
    private lateinit var viewModel: WorkoutSession_ViewModel
    private lateinit var adapter: WorkoutHistoryDetailsAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        // Default onViewCreated constructor
        super.onViewCreated(view, savedInstanceState)


        // Get passed argument
        val sessionId = arguments?.getInt("sessionId") ?: return
        val workoutName = arguments?.getString("workoutName") ?: "Workout"


        // Fetch views
        val title = view.findViewById<TextView>(R.id.historyDetailsTitle)
        title.text = workoutName


        // RecyclerView stuff
        val recyclerView = view.findViewById<RecyclerView>(R.id.historyDetailsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = WorkoutHistoryDetailsAdapter()
        recyclerView.adapter = adapter


        // Database stuffs
        val database = WorkoutDatabase.getDatabase(requireContext())
        val repository = WorkoutSession_Repository(database.workoutSessionDao())
        val factory = WorkoutSession_ViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[WorkoutSession_ViewModel::class.java]


        // Load the adapter
        viewModel.Read_WorkoutSet_WorkoutSetDetails(sessionId) { sets ->
            adapter.submitList(sets)
        }
    }
}