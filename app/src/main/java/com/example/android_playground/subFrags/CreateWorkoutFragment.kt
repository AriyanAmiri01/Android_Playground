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
import com.example.android_playground.repository.Exercise_Repository
import com.example.android_playground.repository.Workout_ViewModel
import com.example.android_playground.repository.Workout_ViewModelFactory
import com.example.android_playground.repository.Workout_Repository
import android.widget.Button
import android.widget.EditText
import com.example.android_playground.repository.Exercise_ViewModel
import com.example.android_playground.repository.Exercise_ViewModelFactory


class CreateWorkoutFragment : Fragment(R.layout.x02_sub_frag_create_workout) {

    // Variables
    private val selectedExercises = mutableListOf<Exercise>()
    private lateinit var selectedAdapter: X02_Sub_SelectedExerciseAdapter
    lateinit var exerciseViewModel: Exercise_ViewModel;
    private lateinit var workoutViewModel : Workout_ViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        // Default onViewCreated constructor
        super.onViewCreated(view, savedInstanceState)


        // Fetch views
        val workoutNameEditText = view.findViewById<EditText>(R.id.workoutNameEditText)
        val workoutDurationEditText = view.findViewById<EditText>(R.id.workoutDurationEditText)
        val createWorkoutButton = view.findViewById<Button>(R.id.createWorkoutButton)


        // Database stuffs
        val database = WorkoutDatabase.getDatabase(requireContext())
        val exerciseRepository = Exercise_Repository(database.exerciseDao())
        val workoutRepository = Workout_Repository(database.workoutDao())
        val exerciseViewModelFactory = Exercise_ViewModelFactory(exerciseRepository)
        val workoutLibraryViewModelFactory = Workout_ViewModelFactory(workoutRepository)
        exerciseViewModel = ViewModelProvider(this, exerciseViewModelFactory)[Exercise_ViewModel::class.java]
        workoutViewModel = ViewModelProvider(this, workoutLibraryViewModelFactory)[Workout_ViewModel::class.java]


        // RecyclerView Stuffs
        val selectedRecyclerView = view.findViewById<RecyclerView>(R.id.selectedExercisesRecyclerView)
        selectedAdapter = X02_Sub_SelectedExerciseAdapter(exercises = selectedExercises, onRemoveClick = { exercise ->

            selectedExercises.remove(exercise)

            selectedAdapter.notifyDataSetChanged()
        })
        selectedRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        selectedRecyclerView.adapter = selectedAdapter
        val availableRecyclerView = view.findViewById<RecyclerView>(R.id.availableExercisesRecyclerView)
        availableRecyclerView.layoutManager = LinearLayoutManager(requireContext())


        // Load available addapter
        exerciseViewModel.Read_AllExercises { exercises ->
            val availableAdapter = X02_Sub_ExerciseSelectionAdapter(exercises = exercises, onAddClick = { exercise ->
                    if (!selectedExercises.contains(exercise)) {
                        selectedExercises.add(exercise)
                        selectedAdapter.notifyDataSetChanged()
                    }
                })
            availableRecyclerView.adapter = availableAdapter
        }


        // Event listeners
        createWorkoutButton.setOnClickListener{

            // 1. Get values
            val workoutName = workoutNameEditText.text.toString().trim()
            val durationText = workoutDurationEditText.text.toString().trim()
            val duration = durationText.toInt()

            // 2. Validated values
            if(workoutName.isEmpty()){
                workoutNameEditText.error = "Enter a workout name"
                return@setOnClickListener
            }
            if (durationText.isEmpty()) {
                workoutDurationEditText.error = "Enter a duration"
                return@setOnClickListener
            }

            // 3. Create data to be passed to viewModel
            val workout = Workout(name = workoutName, duration = duration)
            val exerciseIds = selectedExercises.map { exercise ->
                exercise.id
            }

            // 4. Create workout in database
            workoutViewModel.Create_Workout(
                workout = workout,
                exerciseIds = exerciseIds,
                onComplete = { parentFragmentManager.popBackStack() }
            )
        }


    }
}