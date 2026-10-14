package com.example.android_playground.mainFrags

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android_playground.R
import com.example.android_playground.data.WorkoutDatabase
import com.example.android_playground.data.WorkoutSetDetails
import com.example.android_playground.adapters.X05_Main_WorkoutHistoryAdapter
import com.example.android_playground.repository.WorkoutSession_ViewModel
import com.example.android_playground.repository.WorkoutSession_ViewModelFactory
import com.example.android_playground.repository.WorkoutSession_Repository


data class WorkoutHistoryExercise(
    val exerciseId: Int,
    val exerciseName: String,
    val sets: List<WorkoutSetDetails>
)


class K05_HistoryFragment : Fragment(R.layout.x05_main_frag_history) {

    // Variables
    private lateinit var viewModel: WorkoutSession_ViewModel
    private lateinit var adapter: X05_Main_WorkoutHistoryAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        // Default onViewCreated constructor
        super.onViewCreated(view, savedInstanceState)


        // Database Stuffs
        val database = WorkoutDatabase.getDatabase(requireContext())
        val repository = WorkoutSession_Repository(database.workoutSessionDao())
        val factory = WorkoutSession_ViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[WorkoutSession_ViewModel::class.java]


        // RecyclerView Stuffs
        val recyclerView = view.findViewById<RecyclerView>(R.id.workoutHistoryRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = X05_Main_WorkoutHistoryAdapter { session ->
            val bundle = Bundle()
            bundle.putInt("sessionId", session.sessionId)
            bundle.putString("workoutName", session.workoutName)
            findNavController().navigate(R.id.action_historyFragment_to_workoutHistoryDetailsFragment, bundle)
        }
        recyclerView.adapter = adapter


        // Load completed workouts.
        viewModel.Read_WorkoutSession_Completed { sessions ->
            adapter.submitList(sessions)
        }
    }
}