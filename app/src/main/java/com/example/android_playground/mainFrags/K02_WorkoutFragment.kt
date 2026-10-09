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
import com.example.android_playground.adapters.WorkoutAdapter
import com.example.android_playground.repository.ActiveWorkoutViewModelFactory
import com.example.android_playground.repository.ActiveWorkoutViewModel
import com.example.android_playground.repository.WorkoutRepository
import com.example.android_playground.repository.WorkoutSessionRepository
import com.example.android_playground.repository.WorkoutViewModel
import com.example.android_playground.repository.WorkoutViewModelFactory
import kotlinx.coroutines.launch

class K02_WorkoutFragment : Fragment(R.layout.x02_main_frag_workouts) {

    // Room + Firestore
    private lateinit var viewModel: WorkoutViewModel
    private lateinit var activeWorkoutViewModel: ActiveWorkoutViewModel


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Notify the childrens?
        super.onViewCreated(view, savedInstanceState)

        // Get the database
        val database = WorkoutDatabase.getDatabase(requireContext())

        // ROOM COMMUNICATIONS
        val workoutRepository = WorkoutRepository(database.workoutDao())
        val sessionRepository = WorkoutSessionRepository(database.workoutSessionDao())
        val workoutFactory = WorkoutViewModelFactory(workoutRepository)
        val activeWorkoutFactory = ActiveWorkoutViewModelFactory(sessionRepository)
        viewModel = ViewModelProvider(this, workoutFactory)[WorkoutViewModel::class.java]
        activeWorkoutViewModel = ViewModelProvider(this, activeWorkoutFactory)[ActiveWorkoutViewModel::class.java]

        // Create the recyclerView
        val recyclerView = view.findViewById<RecyclerView>(R.id.workoutRecyclerView)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)


        // Observe workouts
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.syncWorkouts()
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                viewModel.getAllWorkouts().collect { workouts ->

                    val adapter = WorkoutAdapter(
                        workouts = workouts,

                        onDeleteClick = { workout ->
                            viewModel.deleteWorkout(workout) {
                            }
                        },

                        onWorkoutClick = { workout ->

                            val bundle = Bundle().apply {
                                putInt(
                                    "workoutId",
                                    workout.id
                                )
                            }

                            findNavController().navigate(
                                R.id.action_workoutLibraryFragment_to_workoutDetailsFragment,
                                bundle
                            )
                        },

                        onStartClick = { workout ->

                            activeWorkoutViewModel.startWorkout(
                                workout.id
                            ) { sessionId ->

                                val bundle = Bundle().apply {
                                    putInt("workoutId", workout.id)
                                    putInt("sessionId", sessionId)
                                }

                                findNavController().navigate(
                                    R.id.action_workoutLibraryFragment_to_activeWorkoutFragment,
                                    bundle
                                )
                            }
                        }
                    )

                    recyclerView.adapter = adapter
                }
            }
        }




        // Create workout button
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



}