package com.example.android_playground.mainFrags

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android_playground.R
import com.example.android_playground.data.WorkoutDatabase
import com.example.android_playground.repository.ExerciseRepository
import com.example.android_playground.repository.ExerciseViewModel
import com.example.android_playground.repository.ExerciseViewModelFactory
import androidx.navigation.fragment.findNavController


class K03_ExercisesFragment : Fragment(R.layout.x03_main_frag_exercises) {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: X03_Main_ExerciseAdapter
    private lateinit var exerciseViewModel: ExerciseViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.exercisesRecyclerView)

        adapter = X03_Main_ExerciseAdapter(emptyList()) { exerciseId ->

            val bundle = Bundle().apply {
                putInt("exerciseId", exerciseId)
            }

            findNavController().navigate(
                R.id.exerciseFragment,
                bundle
            )
        }

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        val database = WorkoutDatabase.getDatabase(requireContext())
        val repository = ExerciseRepository(database.exerciseDao())

        exerciseViewModel = ViewModelProvider(
            this,
            ExerciseViewModelFactory(repository)
        )[ExerciseViewModel::class.java]

        exerciseViewModel.getAllExercises { exercises ->

            adapter.updateExercises(exercises)
        }
    }
}