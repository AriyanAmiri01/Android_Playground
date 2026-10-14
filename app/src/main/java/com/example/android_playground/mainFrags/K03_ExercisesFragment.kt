package com.example.android_playground.mainFrags

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android_playground.R
import com.example.android_playground.data.WorkoutDatabase
import com.example.android_playground.repository.Exercise_Repository
import com.example.android_playground.repository.Exercise_ViewModel
import com.example.android_playground.repository.Exercise_ViewModelFactory
import androidx.navigation.fragment.findNavController


class K03_ExercisesFragment : Fragment(R.layout.x03_main_frag_exercises) {

    // Variables
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: X03_Main_ExerciseAdapter
    private lateinit var exerciseViewModel: Exercise_ViewModel


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        // Default onViewCreated constructor
        super.onViewCreated(view, savedInstanceState)


        // Database stuffs
        val database = WorkoutDatabase.getDatabase(requireContext())
        val repository = Exercise_Repository(database.exerciseDao())
        exerciseViewModel = ViewModelProvider(this, Exercise_ViewModelFactory(repository))[Exercise_ViewModel::class.java]


        // Recycler view stuffs
        recyclerView = view.findViewById(R.id.exercisesRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = X03_Main_ExerciseAdapter(emptyList()) { exerciseId ->
            val bundle = Bundle().apply { putInt("exerciseId", exerciseId) }
            findNavController().navigate(R.id.exerciseFragment, bundle)
        }
        recyclerView.adapter = adapter
        exerciseViewModel.Read_AllExercises { exercises ->
            adapter.updateExercises(exercises)
        }
    }
}