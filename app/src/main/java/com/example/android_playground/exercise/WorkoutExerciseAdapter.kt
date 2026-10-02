package com.example.android_playground.exercise

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android_playground.R
import com.example.android_playground.data.WorkoutExerciseDetails

class WorkoutExerciseAdapter(private val onExerciseClick: (Int) -> Unit) : RecyclerView.Adapter<WorkoutExerciseAdapter.WorkoutExerciseViewHolder>()
{
    // Container and the mothod to fill it
    private var exercises = emptyList<WorkoutExerciseDetails>()
    fun submitList(newExercises: List<WorkoutExerciseDetails>) {
        exercises = newExercises
        notifyDataSetChanged()
    }


    // Visual Sturff
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutExerciseViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_workout_exercise, parent, false)

        return WorkoutExerciseViewHolder(view)
    }


    // Adapter stuffs
    override fun onBindViewHolder(holder: WorkoutExerciseViewHolder, position: Int) {
        holder.bind(exercises[position], onExerciseClick)
    }
    override fun getItemCount(): Int {
        return exercises.size
    }
    class WorkoutExerciseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // @ description: its job is to hold references to one row in recycler row and
        // and make the convenient function to puts those items in the recyucler view

        // Get fields in the each item
        private val exerciseName = itemView.findViewById<TextView>(R.id.exerciseNameTextView)
        private val muscleGroup = itemView.findViewById<TextView>(R.id.muscleGroupTextView)
        private val sets = itemView.findViewById<TextView>(R.id.setsTextView)
        private val repetitions = itemView.findViewById<TextView>(R.id.repetitionsTextView)

        // Bind its data
        fun bind(exercise: WorkoutExerciseDetails, onExerciseClick: (Int) -> Unit) {
            exerciseName.text = exercise.name
            muscleGroup.text = exercise.muscleGroup
            sets.text = "Sets: ${exercise.sets}"
            repetitions.text = "Reps: ${exercise.repetitions}"

            itemView.setOnClickListener {
                onExerciseClick(exercise.exerciseId)
            }
        }
    }
}