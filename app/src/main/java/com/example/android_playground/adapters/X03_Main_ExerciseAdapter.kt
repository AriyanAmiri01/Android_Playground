package com.example.android_playground.mainFrags

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android_playground.R
import com.example.android_playground.data.Exercise
import android.widget.ImageView

class X03_Main_ExerciseAdapter(private var exercises: List<Exercise>, private val onExerciseClick: (Int) -> Unit) : RecyclerView.Adapter<X03_Main_ExerciseAdapter.ExerciseViewHolder>() {

    // 1. Wrapper class for one rercyclerView  item View
    class ExerciseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val exerciseImage: ImageView = itemView.findViewById(R.id.exerciseImage)
        val exerciseName: TextView = itemView.findViewById(R.id.exerciseName)
        val exerciseMuscleGroup: TextView = itemView.findViewById(R.id.exerciseMuscleGroup)
    }


    // 2. inflate the xml layout into a view and wraps that inside a viewholder wrapper and returns it
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.x03_main_item_exercise_2, parent, false)
        return ExerciseViewHolder(view)
    }


    // 3. Fills the actual data
    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        val exercise = exercises[position]
        holder.exerciseName.text = exercise.name
        holder.exerciseMuscleGroup.text = exercise.muscleGroup
        when (exercise.muscleGroup.lowercase()) {
            "biceps" -> {
                holder.exerciseImage.setImageResource(R.drawable.musclegroup_bicept)
            }
            "chest" -> {
                holder.exerciseImage.setImageResource(R.drawable.musclegroup_chest)
            }

            "shoulders" -> {
                holder.exerciseImage.setImageResource(R.drawable.musclegroup_shoulder)
            }

            "calves" -> {
                holder.exerciseImage.setImageResource(R.drawable.musclegroup_calf)
            }

            "triceps" -> {
                holder.exerciseImage.setImageResource(R.drawable.musclegroup_tricept)
            }

            "back" -> {
                holder.exerciseImage.setImageResource(R.drawable.musclegroup_back)
            }
        }
        holder.itemView.setOnClickListener {
            onExerciseClick(exercise.id)
        }
    }


    // 4. description responsable for telling the recycler view how many items are there which is used by recyclerView
    override fun getItemCount(): Int = exercises.size

    fun updateExercises(newExercises: List<Exercise>) {
        exercises = newExercises
        notifyDataSetChanged()
    }
}