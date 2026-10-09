package com.example.android_playground.subFrags

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android_playground.R
import com.example.android_playground.data.Exercise
import com.example.android_playground.data.Workout
import com.example.android_playground.data.WorkoutDatabase
import com.example.android_playground.adapters.X02_Sub_ExerciseSelectionAdapter
import com.example.android_playground.adapters.X02_Sub_SelectedExerciseAdapter
import com.example.android_playground.repository.ExerciseRepository
import com.example.android_playground.repository.WorkoutViewModel
import com.example.android_playground.repository.WorkoutViewModelFactory
import com.example.android_playground.repository.WorkoutRepository
import android.widget.Button
import android.widget.EditText
import com.example.android_playground.repository.ExerciseViewModel
import com.example.android_playground.repository.ExerciseViewModelFactory


class CreateWorkoutFragment : Fragment(R.layout.x02_sub_frag_create_workout) {

    private val selectedExercises = mutableListOf<Exercise>()
    private lateinit var selectedAdapter: X02_Sub_SelectedExerciseAdapter
    lateinit var exerciseViewModel: ExerciseViewModel;
    private lateinit var workoutViewModel : WorkoutViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Notify all childrens
        super.onViewCreated(view, savedInstanceState)


        // Setup Selected Exercises RecyclerView
        val selectedRecyclerView = view.findViewById<RecyclerView>(R.id.selectedExercisesRecyclerView)
        selectedAdapter = X02_Sub_SelectedExerciseAdapter(exercises = selectedExercises, onRemoveClick = { exercise ->

            selectedExercises.remove(exercise)

            selectedAdapter.notifyDataSetChanged()
        })
        selectedRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        selectedRecyclerView.adapter = selectedAdapter


        // Setup Available Exercises RecyclerView
        val availableRecyclerView = view.findViewById<RecyclerView>(R.id.availableExercisesRecyclerView)
        availableRecyclerView.layoutManager = LinearLayoutManager(requireContext())


        // Instantiate database and repositories
        val database = WorkoutDatabase.getDatabase(requireContext())
        val exerciseRepository = ExerciseRepository(database.exerciseDao())
        val workoutRepository = WorkoutRepository(database.workoutDao())


        // Instantiate viewModels
        val exerciseViewModelFactory = ExerciseViewModelFactory(exerciseRepository)
        val workoutLibraryViewModelFactory = WorkoutViewModelFactory(workoutRepository)
        exerciseViewModel = ViewModelProvider(this, exerciseViewModelFactory)[ExerciseViewModel::class.java]
        workoutViewModel = ViewModelProvider(this, workoutLibraryViewModelFactory)[WorkoutViewModel::class.java]


        // Load exercises from Room and attach adapter
        exerciseViewModel.getAllExercises { exercises ->
            val availableAdapter = X02_Sub_ExerciseSelectionAdapter(exercises = exercises, onAddClick = { exercise ->
                    if (!selectedExercises.contains(exercise)) {
                        selectedExercises.add(exercise)
                        selectedAdapter.notifyDataSetChanged()
                    }
                })
            availableRecyclerView.adapter = availableAdapter
        }


        // Setup Create Workout Button
        val workoutNameEditText = view.findViewById<EditText>(R.id.workoutNameEditText)
        val workoutDurationEditText = view.findViewById<EditText>(R.id.workoutDurationEditText)
        val createWorkoutButton = view.findViewById<Button>(R.id.createWorkoutButton)

        // Add the eventListener
        createWorkoutButton.setOnClickListener{
            // Fill structure
            val workoutName = workoutNameEditText.text.toString().trim()
            if(workoutName.isEmpty()){
                workoutNameEditText.error = "Enter a workout name"
                return@setOnClickListener
            }

            val durationText = workoutDurationEditText.text.toString().trim()
            if (durationText.isEmpty()) {
                workoutDurationEditText.error = "Enter a duration"
                return@setOnClickListener
            }
            val duration = durationText.toInt()

            // Create the workout
            val workout = Workout(
                name = workoutName,
                duration = duration
            )

            // map its exercises
            val exerciseIds = selectedExercises.map { exercise ->
                exercise.id
            }

            // Let the viewmodel handle the rest while adding a callback function for reverse navigation
            workoutViewModel.createWorkout(workout = workout, exerciseIds = exerciseIds, onComplete = {
                    parentFragmentManager.popBackStack()
                })
        }


    }
}