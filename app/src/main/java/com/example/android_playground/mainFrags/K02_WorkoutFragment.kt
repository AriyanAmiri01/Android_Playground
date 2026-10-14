package com.example.android_playground.mainFrags

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android_playground.R
import com.example.android_playground.data.WorkoutDatabase
import com.example.android_playground.adapters.X02_Main_WorkoutAdapter
import com.example.android_playground.repository.WorkoutSession_ViewModelFactory
import com.example.android_playground.repository.WorkoutSession_ViewModel
import com.example.android_playground.repository.Workout_Repository
import com.example.android_playground.repository.WorkoutSession_Repository
import com.example.android_playground.repository.Workout_ViewModel
import com.example.android_playground.repository.Workout_ViewModelFactory
import kotlinx.coroutines.launch

class K02_WorkoutFragment : Fragment(R.layout.x02_main_frag_workouts) {

    // Variables
    private lateinit var viewModel: Workout_ViewModel
    private lateinit var activeWorkoutViewModel: WorkoutSession_ViewModel


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        // Default onViewCreated constructor
        super.onViewCreated(view, savedInstanceState)


        // Database stuffs
        val database = WorkoutDatabase.getDatabase(requireContext())
        val workoutRepository = Workout_Repository(database.workoutDao())
        val sessionRepository = WorkoutSession_Repository(database.workoutSessionDao())
        val workoutFactory = Workout_ViewModelFactory(workoutRepository)
        val activeWorkoutFactory = WorkoutSession_ViewModelFactory(sessionRepository)
        viewModel = ViewModelProvider(this, workoutFactory)[Workout_ViewModel::class.java]
        activeWorkoutViewModel = ViewModelProvider(this, activeWorkoutFactory)[WorkoutSession_ViewModel::class.java]


        // RecyclerView stuffs
        val recyclerView = view.findViewById<RecyclerView>(R.id.workoutRecyclerView)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        // Attach an empty adapter immediately to fix warnings
        recyclerView.adapter = X02_Main_WorkoutAdapter(
            workouts = emptyList(),
            onDeleteClick = { workout -> viewModel.Delete_Workout(workout) {} },
            onWorkoutClick = { workout ->
                val bundle = Bundle().apply { putInt("workoutId", workout.id) }
                findNavController().navigate(R.id.action_workoutLibraryFragment_to_workoutDetailsFragment, bundle) },
            onStartClick = { workout ->
                activeWorkoutViewModel.Create_WorkoutSession(workout.id) { sessionId ->
                    val bundle = Bundle().apply { putInt("workoutId", workout.id);putInt("sessionId", sessionId); }
                    findNavController().navigate(R.id.action_workoutLibraryFragment_to_activeWorkoutFragment, bundle) } }
        )

        // Sync and Read workouts
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.Sync_Workouts()
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                viewModel.Read_AllWorkouts().collect { workouts ->
                    val adapter = X02_Main_WorkoutAdapter(
                        workouts = workouts,
                        onDeleteClick = { workout -> viewModel.Delete_Workout(workout) {} },
                        onWorkoutClick = { workout -> val bundle = Bundle().apply{ putInt("workoutId", workout.id) }
                            findNavController().navigate(R.id.action_workoutLibraryFragment_to_workoutDetailsFragment, bundle) },
                        onStartClick = { workout -> activeWorkoutViewModel.Create_WorkoutSession(workout.id) { sessionId ->
                                val bundle = Bundle().apply { putInt("workoutId", workout.id)
                                    putInt("sessionId", sessionId)
                                }
                                findNavController().navigate(R.id.action_workoutLibraryFragment_to_activeWorkoutFragment, bundle)
                            }
                        }
                    )

                    recyclerView.adapter = adapter
                }
            }
        }


        // Create workout button
        val createWorkoutButton = view.findViewById<Button>(R.id.createWorkoutButton)
        createWorkoutButton.setOnClickListener {
            findNavController().navigate(R.id.action_workoutLibraryFragment_to_createWorkoutFragment)
        }
    }
}