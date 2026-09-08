package com.example.android_playground


import android.view.LayoutInflater  // For turning XML layout into actual view objects
import android.view.View // Base class for UI elements
import android.view.ViewGroup // View container which inherit view itself
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView // A catalogue area for displaying elements
import android.widget.ImageButton


class WorkoutAdapter( private val workouts: List<Workout>) : RecyclerView.Adapter<WorkoutAdapter.WorkoutViewHolder>()
{
    // @ description Prepare the data before passed to the recyclerView

    class WorkoutViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        // @ description represent one displayed item
        val workoutName: TextView =
            itemView.findViewById(R.id.workoutName)
        val workoutDuration: TextView =
            itemView.findViewById(R.id.workoutDuration)

        val deleteButton: ImageButton =
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
        val workout = workouts[position]

        holder.workoutName.text = workout.name
        holder.workoutDuration.text = workout.duration
        holder.deleteButton.setOnClickListener{

        }
    }

    // @ description responsable for telling the recycler view how many items are there
    override fun getItemCount(): Int {
        return workouts.size
    }
}