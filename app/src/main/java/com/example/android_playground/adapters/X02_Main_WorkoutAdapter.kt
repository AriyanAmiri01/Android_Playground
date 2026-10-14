package com.example.android_playground.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android_playground.R
import com.example.android_playground.data.Workout

class X02_Main_WorkoutAdapter(
    private val workouts: List<Workout>,
    private val onDeleteClick: (Workout) -> Unit,
    private val onWorkoutClick: (Workout) -> Unit,
    private val onStartClick: (Workout) -> Unit
) : RecyclerView.Adapter<X02_Main_WorkoutAdapter.WorkoutViewHolder>()
{

    // 1. Wrapper class for one rercyclerView  item View
    class WorkoutViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val workoutName: TextView = itemView.findViewById(R.id.workoutName)
        val workoutDuration: TextView = itemView.findViewById(R.id.workoutDuration)
        val startButton: Button = itemView.findViewById(R.id.StartWorkoutButton)
        val deleteButton: Button = itemView.findViewById(R.id.deleteButton)
    }


    // 2. inflate the xml layout into a view and wraps that inside a viewholder wrapper and returns it
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.x02_main_item_workout, parent, false)
        return WorkoutViewHolder(view)
    }


    // 3. Fills the actual data
    override fun onBindViewHolder(holder: WorkoutViewHolder, position: Int) {
        // Fetch the workout
        val workout = workouts[position]

        // Set the Holder Valeu
        holder.workoutName.text = workout.name
        holder.workoutDuration.text = "${workout.duration} minutes"

        // Add deletion event listener
        holder.startButton.setOnClickListener { onStartClick(workout) }
        holder.deleteButton.setOnClickListener { onDeleteClick(workout) }
        holder.itemView.setOnClickListener { onWorkoutClick(workout) }
    }


    // 4. description responsable for telling the recycler view how many items are there which is used by recyclerView
    override fun getItemCount(): Int {
        return workouts.size
    }
}