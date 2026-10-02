package com.example.android_playground.workoutLibrary

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android_playground.R
import com.example.android_playground.data.WorkoutDatabase
import com.example.android_playground.data.WorkoutExerciseDetails
import com.example.android_playground.exercise.WorkoutExerciseAdapter
import com.example.android_playground.repository.ExerciseRepository
import com.example.android_playground.repository.ExerciseViewModel
import com.example.android_playground.repository.ExerciseViewModelFactory
import com.example.android_playground.repository.WorkoutRepository

class WorkoutDetailsFragment : Fragment(R.layout.fragment_workout_details) {

    private lateinit var viewModel: ExerciseViewModel
    private lateinit var adapter: WorkoutExerciseAdapter

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        // 0. Call the Fragment's implementation
        super.onViewCreated(view, savedInstanceState)

        // 1. Get values passed by argument
        val workoutId = arguments?.getInt("workoutId") ?: return


        // 2. Get adapter and recycler view ready
        fun adapterCallback(exerciseId : Int){
            val bundle = Bundle().apply {
                putInt("exerciseId", exerciseId)
            }

            findNavController().navigate(
                R.id.action_workoutDetailsFragment_to_exerciseFragment,
                bundle
            )
        }
        adapter = WorkoutExerciseAdapter(::adapterCallback)

        val recyclerView = view.findViewById<RecyclerView>(R.id.workoutExercisesRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
        val workoutTitle = view.findViewById<TextView>(R.id.workoutTitleTextView)


        // 3. Room communications
        val databaseRef = WorkoutDatabase.getDatabase(requireContext())
        val exerciseDao = databaseRef.exerciseDao()
        val repository = ExerciseRepository(exerciseDao = exerciseDao)
        val factory = ExerciseViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[ExerciseViewModel::class.java]


        // 4. Load into the adapter
        fun onExercisesLoaded(exercises: List<WorkoutExerciseDetails>) { adapter.submitList(exercises) }
        viewModel.loadExercisesForWorkout(workoutId, ::onExercisesLoaded)

    }
}