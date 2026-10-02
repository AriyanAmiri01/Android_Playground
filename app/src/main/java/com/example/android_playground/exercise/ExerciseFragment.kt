package com.example.android_playground.exercise

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.VideoView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.android_playground.R
import com.example.android_playground.data.WorkoutDatabase
import com.example.android_playground.repository.AuthRepository
import com.example.android_playground.repository.ExerciseRepository
import com.example.android_playground.repository.ExerciseViewModel
import com.example.android_playground.repository.ExerciseViewModelFactory
import kotlinx.coroutines.launch

class ExerciseFragment : Fragment(R.layout.sub_frag_exercise) {

    // Room variables
    private lateinit var viewModel: ExerciseViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Call defualt constructor
        super.onViewCreated(view, savedInstanceState)

        // Get the exercise ID passed through Navigation
        val exerciseId = arguments?.getInt("exerciseId") ?: return
        Log.d("ExerciseFragment", "Received exerciseId: $exerciseId")

        // Room conection
        val databaseRef = WorkoutDatabase.getDatabase(requireContext())
        val exerciseDao = databaseRef.exerciseDao()
        val repository = ExerciseRepository(exerciseDao)
        val authRepo = AuthRepository()
        val factory = ExerciseViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[ExerciseViewModel::class.java]

        // Load the exercise
        viewModel.getExerciseById(exerciseId){exercise ->
            if(exercise != null){
                // Get the view refences
                val exerciseNameTextView = view.findViewById<TextView>(R.id.exerciseNameTextView)
                val muscleGroupTextView = view.findViewById<TextView>(R.id.muscleGroupTextView)

                // Set their values
                exerciseNameTextView.text = exercise.name
                muscleGroupTextView.text = exercise.muscleGroup

                viewLifecycleOwner.lifecycleScope.launch {

                    // Get download URL
                    val url = authRepo.getDownloadUrl(exercise.videoUrl.toString())

                    // Set and start the video
                    val videoView = view.findViewById<VideoView>(R.id.exerciseVideoView)
                    videoView.setVideoURI(Uri.parse(url))
                    videoView.start()
                }




            }else{
                // Output error message
                Log.e("ExerciseFragment", "Exercise not found: $exerciseId")
            }
        }





    }
}

