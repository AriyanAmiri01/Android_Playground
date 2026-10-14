package com.example.android_playground.mainFrags

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android_playground.R
import com.example.android_playground.data.WorkoutDatabase
import com.example.android_playground.data.WorkoutExerciseDetails
import com.example.android_playground.adapters.X04_Main_ActiveWorkoutAdapter
import com.example.android_playground.repository.WorkoutSession_ViewModel
import com.example.android_playground.repository.Exercise_Repository
import com.example.android_playground.repository.Exercise_ViewModel
import com.example.android_playground.repository.Exercise_ViewModelFactory

import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.example.android_playground.repository.WorkoutSession_ViewModelFactory
import com.example.android_playground.repository.WorkoutSession_Repository

class K04_ActiveWorkoutFragment : Fragment(R.layout.x04_main_frag_active_workout) {

    // Variables
    private lateinit var exerciseViewModel: Exercise_ViewModel
    private lateinit var activeWorkoutViewModel: WorkoutSession_ViewModel
    private lateinit var adapter: X04_Main_ActiveWorkoutAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        // Default onViewCreated constructor
        super.onViewCreated(view, savedInstanceState)

        // Extract passed arguments
        val workoutId = arguments?.getInt("workoutId") ?: return
        val sessionId = arguments?.getInt("sessionId") ?: return
        if (workoutId == -1 || sessionId == -1) { return }


        // Database stuffs
        val database = WorkoutDatabase.getDatabase(requireContext())
        val sessionRepository = WorkoutSession_Repository(database.workoutSessionDao())
        val activeWorkoutFactory = WorkoutSession_ViewModelFactory(sessionRepository)
        activeWorkoutViewModel = ViewModelProvider(this, activeWorkoutFactory)[WorkoutSession_ViewModel::class.java]
        val exerciseRepository = Exercise_Repository(database.exerciseDao())
        val exerciseFactory = Exercise_ViewModelFactory(exerciseRepository)
        exerciseViewModel = ViewModelProvider(this, exerciseFactory)[Exercise_ViewModel::class.java]


        // RecyclerView Stuffs
        val recyclerView = view.findViewById<RecyclerView>(R.id.activeWorkoutRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = X04_Main_ActiveWorkoutAdapter()
        recyclerView.adapter = adapter


       // Load the exercise inside the adapter
        fun onExercisesLoaded(exercises: List<WorkoutExerciseDetails>) {
            // The callback that receives the data from the ViewModel and submits it to the adapter.
            adapter.submitList(exercises)
        }
        exerciseViewModel.Read_ExercisesForWorkout(workoutId, ::onExercisesLoaded)


        // Event Listeners
        val finishWorkoutButton = view.findViewById<Button>(R.id.finishWorkoutButton)
        finishWorkoutButton.setOnClickListener {
            // Get all entered sets from the adapter.
            val workoutSets = adapter.getWorkoutSets(sessionId)

            // Save the sets and mark the session completed.
            activeWorkoutViewModel.Update_WorkoutSession_FinishAndAddSets(sessionId = sessionId, workoutSets = workoutSets) {
                findNavController().popBackStack()
            }
        }
    }
}