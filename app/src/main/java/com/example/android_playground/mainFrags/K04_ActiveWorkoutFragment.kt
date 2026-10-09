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
import com.example.android_playground.adapters.ActiveWorkoutAdapter
import com.example.android_playground.repository.ActiveWorkoutViewModel
import com.example.android_playground.repository.ExerciseRepository
import com.example.android_playground.repository.ExerciseViewModel
import com.example.android_playground.repository.ExerciseViewModelFactory

import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.example.android_playground.repository.ActiveWorkoutViewModelFactory
import com.example.android_playground.repository.WorkoutSessionRepository

class K04_ActiveWorkoutFragment :
    Fragment(R.layout.x04_main_frag_active_workout) {

    private lateinit var exerciseViewModel: ExerciseViewModel
    private lateinit var activeWorkoutViewModel: ActiveWorkoutViewModel
    private lateinit var adapter: ActiveWorkoutAdapter

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)


        // 1. Get the workout and session IDs passed from WorkoutFragment.
        val workoutId =
            arguments?.getInt("workoutId") ?: return

        val sessionId =
            arguments?.getInt("sessionId") ?: return


        // 2. Make sure valid IDs were received.
        if (workoutId == -1 || sessionId == -1) {
            return
        }


        // 3. Create the adapter.
        adapter = ActiveWorkoutAdapter()


        // 4. Configure the RecyclerView.
        val recyclerView =
            view.findViewById<RecyclerView>(
                R.id.activeWorkoutRecyclerView
            )

        recyclerView.layoutManager =
            LinearLayoutManager(requireContext())

        recyclerView.adapter = adapter


        // 5. Get our Room database.
        val database =
            WorkoutDatabase.getDatabase(requireContext())

        val sessionRepository =
            WorkoutSessionRepository(
                database.workoutSessionDao()
            )

        val activeWorkoutFactory =
            ActiveWorkoutViewModelFactory(
                sessionRepository
            )

        activeWorkoutViewModel =
            ViewModelProvider(
                this,
                activeWorkoutFactory
            )[ActiveWorkoutViewModel::class.java]

        // 6. Create the Exercise repository.
        val exerciseRepository =
            ExerciseRepository(
                database.exerciseDao()
            )


        // 7. Create the Exercise ViewModel.
        val exerciseFactory =
            ExerciseViewModelFactory(exerciseRepository)

        exerciseViewModel = ViewModelProvider(
            this,
            exerciseFactory
        )[ExerciseViewModel::class.java]


        // 8. Receive the exercises from Room.
        fun onExercisesLoaded(
            exercises: List<WorkoutExerciseDetails>
        ) {
            adapter.submitList(exercises)
        }


        // 9. Load the exercises belonging to this workout.
        exerciseViewModel.loadExercisesForWorkout(
            workoutId,
            ::onExercisesLoaded
        )



        val finishWorkoutButton =
            view.findViewById<Button>(
                R.id.finishWorkoutButton
            )
        finishWorkoutButton.setOnClickListener {

            // Get all entered sets from the adapter.
            val workoutSets =
                adapter.getWorkoutSets(
                    sessionId
                )


            // Save the sets and mark the session completed.
            activeWorkoutViewModel.completeWorkout(
                sessionId = sessionId,
                workoutSets = workoutSets
            ) {

                // Return to the previous screen.
                findNavController().popBackStack()
            }
        }
    }



}