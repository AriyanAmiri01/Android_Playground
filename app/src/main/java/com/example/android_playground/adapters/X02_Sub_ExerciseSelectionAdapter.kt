package com.example.android_playground.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android_playground.R
import com.example.android_playground.data.Exercise

class X02_Sub_ExerciseSelectionAdapter(
    private val exercises: List<Exercise>,
    private val onAddClick: (Exercise) -> Unit  // A parameter which is a function that receives a Exercise parameter and returns unit
) : RecyclerView.Adapter<X02_Sub_ExerciseSelectionAdapter.ExerciseViewHolder>() {

    // 1. Wrapper class for one rercyclerView  item View
    class ExerciseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val exerciseName: TextView = itemView.findViewById(R.id.exerciseNameTextView)
        val actionButton: Button = itemView.findViewById(R.id.exerciseActionButton)
    }


    // 2. inflate the xml layout into a view and wraps that inside a viewholder wrapper and returns it
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.x02_main_item_exercise, parent, false)
        return ExerciseViewHolder(view)
    }


    // 3. Fills the actual data
    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        val exercise = exercises[position]
        holder.exerciseName.text = exercise.name
        holder.actionButton.setOnClickListener { onAddClick(exercise) }
    }


    // 4. description responsable for telling the recycler view how many items are there which is used by recyclerView
    override fun getItemCount(): Int {
        return exercises.size
    }
}