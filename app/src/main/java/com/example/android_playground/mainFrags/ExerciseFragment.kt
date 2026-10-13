package com.example.android_playground.mainFrags

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
import com.example.android_playground.repository.Auth_Repository
import com.example.android_playground.repository.Exercise_Repository
import com.example.android_playground.repository.Exercise_ViewModel
import com.example.android_playground.repository.Exercise_ViewModelFactory
import kotlinx.coroutines.launch

class ExerciseFragment : Fragment(R.layout.x03_sub_frag_exercise) {

    // Room variables
    private lateinit var viewModel: Exercise_ViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Call defualt constructor
        super.onViewCreated(view, savedInstanceState)

        // Get the exercise ID passed through Navigation
        val exerciseId = arguments?.getInt("exerciseId") ?: return
        Log.d("ExerciseFragment", "Received exerciseId: $exerciseId")

        // Room conection
        val databaseRef = WorkoutDatabase.getDatabase(requireContext())
        val exerciseDao = databaseRef.exerciseDao()
        val repository = Exercise_Repository(exerciseDao)
        val authRepo = Auth_Repository()
        val factory = Exercise_ViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[Exercise_ViewModel::class.java]

        // Load the exercise
        viewModel.Read_Exercise(exerciseId){exercise ->
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
                Log.e("ExerciseFragment", "Exercise not found: $exerciseId")
            }
        }
    }
}