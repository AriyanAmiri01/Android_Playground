package com.example.android_playground.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android_playground.R
import com.example.android_playground.data.WorkoutExerciseDetails

class X02_Sub_WorkoutExerciseAdapter(
    private val exercisesDetails : List<WorkoutExerciseDetails>,
    private val onExerciseClick: (Int) -> Unit) : RecyclerView.Adapter<X02_Sub_WorkoutExerciseAdapter.WorkoutExerciseViewHolder>()
{



    // 1. Wrapper class for one rercyclerView  item View
    class WorkoutExerciseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Get fields in the each item
        val exerciseName = itemView.findViewById<TextView>(R.id.exerciseNameTextView)
        val muscleGroup = itemView.findViewById<TextView>(R.id.muscleGroupTextView)
        val sets = itemView.findViewById<TextView>(R.id.setsTextView)
        val repetitions = itemView.findViewById<TextView>(R.id.repetitionsTextView)
    }



    // 2. inflate the xml layout into a view and wraps that inside a viewholder wrapper and returns it
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutExerciseViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.x02_sub_item_workout_exercise, parent, false)

        return WorkoutExerciseViewHolder(view)
    }


    // 3. Fills the actual data
    override fun onBindViewHolder(holder: WorkoutExerciseViewHolder, position: Int) {
        // Fetch exercise
        val exercise = exercisesDetails[position]

        // Bind its stuffs
        holder.exerciseName.text = exercise.name
        holder.muscleGroup.text = exercise.muscleGroup
        holder.sets.text = "Sets: ${exercise.sets}"
        holder.repetitions.text = "Reps: ${exercise.repetitions}"
        holder.itemView.setOnClickListener {
            onExerciseClick(exercise.exerciseId)
        }
    }


    // 4. responsable for telling the recycler view how many items are there which is used by recyclerView
    override fun getItemCount(): Int {
        return exercisesDetails.size
    }
}