package com.example.android_playground.subFrags

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android_playground.R
import com.example.android_playground.adapters.X02_Sub_ExerciseSelectionAdapter
import com.example.android_playground.data.WorkoutDatabase
import com.example.android_playground.data.WorkoutExerciseDetails
import com.example.android_playground.adapters.X02_Sub_WorkoutExerciseAdapter
import com.example.android_playground.repository.Exercise_Repository
import com.example.android_playground.repository.Exercise_ViewModel
import com.example.android_playground.repository.Exercise_ViewModelFactory

class WorkoutDetailsFragment : Fragment(R.layout.x02_sub_recyc_workout_details) {

    // Variables
    private lateinit var viewModel: Exercise_ViewModel
    private lateinit var adapter: X02_Sub_WorkoutExerciseAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        // Default onViewCreated constructor
        super.onViewCreated(view, savedInstanceState)


        // Get passed argument
        val workoutId = arguments?.getInt("workoutId") ?: return


        // Fetch views
        val workoutTitle = view.findViewById<TextView>(R.id.workoutTitleTextView)


        // Database stuffs
        val databaseRef = WorkoutDatabase.getDatabase(requireContext())
        val exerciseDao = databaseRef.exerciseDao()
        val repository = Exercise_Repository(exerciseDao = exerciseDao)
        val factory = Exercise_ViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[Exercise_ViewModel::class.java]


        // Prepare a custom parameter to be bind for each item
        fun onExerciseClick(exerciseId: Int) {
            val bundle = Bundle()
            bundle.putInt("exerciseId", exerciseId)
            findNavController().navigate(R.id.action_workoutDetailsFragment_to_exerciseFragment, bundle)
        }


        // Recycler view stuffs
        val recyclerView = view.findViewById<RecyclerView>(R.id.workoutExercisesRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // 1. Immediately attach an empty adapter
        adapter = X02_Sub_WorkoutExerciseAdapter(emptyList(), ::onExerciseClick)
        recyclerView.adapter = adapter

        // 2. Load exercises
        viewModel.Read_ExercisesForWorkout(workoutId) { exercises ->

            // 3. Replace adapter when data arrives
            adapter = X02_Sub_WorkoutExerciseAdapter(exercises, ::onExerciseClick)
            recyclerView.adapter = adapter
        }
    }
}