package com.example.android_playground.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android_playground.R
import com.example.android_playground.data.Exercise

class SelectedExerciseAdapter(
    private val exercises: List<Exercise>,
    private val onRemoveClick: (Exercise) -> Unit  // A parameter which is a function that receives a Exercise parameter and returns unit
) : RecyclerView.Adapter<SelectedExerciseAdapter.ExerciseViewHolder>() {

    // @ description A container for the datas that are being passed to the recyclerView
    class ExerciseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val exerciseName: TextView =
            itemView.findViewById(R.id.exerciseNameTextView)

        val actionButton: Button =
            itemView.findViewById(R.id.exerciseActionButton) }

    // @ description: Now we create one of those viewholder and inflate its layout from xml
    override fun onCreateViewHolder(
        parent: ViewGroup,  // For handling context?
        viewType: Int       // For allowing different types of recyclerViews
    ): ExerciseViewHolder {
        // Create a android view from xml object
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.x02_main_item_exercise, parent, false)

        return ExerciseViewHolder(view) }

    // @ description: Now we will that viewholder with meaningfull data
    override fun onBindViewHolder(
        holder: ExerciseViewHolder,
        position: Int
    ) {
        val exercise = exercises[position]

        holder.exerciseName.text = exercise.name

        holder.actionButton.setOnClickListener {
            onRemoveClick(exercise) } }

    override fun getItemCount(): Int {
        return exercises.size
    }
}