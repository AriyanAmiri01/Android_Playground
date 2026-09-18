package com.example.android_playground.exercise

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android_playground.R
import com.example.android_playground.data.Workout

class WorkoutAdapter(
    private val workouts: List<Workout>,
    private val onDeleteClick: (Workout) -> Unit
) : RecyclerView.Adapter<WorkoutAdapter.WorkoutViewHolder>()
{
    // @ description Prepare the data before passed to the recyclerView

    class WorkoutViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        // @ description represent one displayed item
        val workoutName: TextView =
            itemView.findViewById(R.id.workoutName)
        val workoutDuration: TextView =
            itemView.findViewById(R.id.workoutDuration)
        val deleteButton: Button =
            itemView.findViewById(R.id.deleteButton)
    }

    // @ description Responsable for create a single ViewHolder
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WorkoutViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_workout, parent, false)
        return WorkoutViewHolder(view)
    }

    // @ description responsable for filling one single item
    override fun onBindViewHolder(
        holder: WorkoutViewHolder,
        position: Int
    ) {
        // Fetch the workout
        val workout = workouts[position]

        // Set the Holder Valeu
        holder.workoutName.text = workout.name
        holder.workoutDuration.text = workout.duration.toString()

        // Add deletion event listener
        holder.deleteButton.setOnClickListener {
            onDeleteClick(workout)
        }
    }

    // @ description responsable for telling the recycler view how many items are there
    override fun getItemCount(): Int {
        return workouts.size
    }
}