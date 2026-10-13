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

class K04_ActiveWorkoutFragment :
    Fragment(R.layout.x04_main_frag_active_workout) {

    private lateinit var exerciseViewModel: Exercise_ViewModel
    private lateinit var activeWorkoutViewModel: WorkoutSession_ViewModel
    private lateinit var adapter: X04_Main_ActiveWorkoutAdapter

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
        adapter = X04_Main_ActiveWorkoutAdapter()


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
            WorkoutSession_Repository(
                database.workoutSessionDao()
            )

        val activeWorkoutFactory =
            WorkoutSession_ViewModelFactory(
                sessionRepository
            )

        activeWorkoutViewModel =
            ViewModelProvider(
                this,
                activeWorkoutFactory
            )[WorkoutSession_ViewModel::class.java]

        // 6. Create the Exercise repository.
        val exerciseRepository =
            Exercise_Repository(
                database.exerciseDao()
            )


        // 7. Create the Exercise ViewModel.
        val exerciseFactory =
            Exercise_ViewModelFactory(exerciseRepository)

        exerciseViewModel = ViewModelProvider(
            this,
            exerciseFactory
        )[Exercise_ViewModel::class.java]


        // 8. Receive the exercises from Room.
        fun onExercisesLoaded(
            exercises: List<WorkoutExerciseDetails>
        ) {
            adapter.submitList(exercises)
        }


        // 9. Load the exercises belonging to this workout.
        exerciseViewModel.Read_ExercisesForWorkout(workoutId, ::onExercisesLoaded)



        val finishWorkoutButton =
            view.findViewById<Button>(
                R.id.finishWorkoutButton
            )
        finishWorkoutButton.setOnClickListener {

            // Get all entered sets from the adapter.
            val workoutSets = adapter.getWorkoutSets(sessionId)


            // Save the sets and mark the session completed.
            activeWorkoutViewModel.Update_WorkoutSession_FinishAndAddSets(sessionId = sessionId, workoutSets = workoutSets) {
                // Return to the previous screen.
                findNavController().popBackStack()
            }
        }
    }



}