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

    class ExerciseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val exerciseImage: ImageView =
            itemView.findViewById(R.id.exerciseImage)

        val exerciseName: TextView =
            itemView.findViewById(R.id.exerciseName)

        val exerciseMuscleGroup: TextView =
            itemView.findViewById(R.id.exerciseMuscleGroup)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.x03_main_item_exercise_2, parent, false)

        return ExerciseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        val exercise = exercises[position]

        holder.exerciseName.text = exercise.name
        holder.exerciseMuscleGroup.text = exercise.muscleGroup

        when (exercise.muscleGroup.lowercase()) {
            "biceps" -> {
                holder.exerciseImage.setImageResource(R.drawable.bicept_image)
            }
        }


        holder.itemView.setOnClickListener {
            onExerciseClick(exercise.id)
        }
    }

    override fun getItemCount(): Int = exercises.size

    fun updateExercises(newExercises: List<Exercise>) {
        exercises = newExercises
        notifyDataSetChanged()
    }
}