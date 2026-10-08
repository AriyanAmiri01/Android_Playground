package com.example.android_playground.exercise

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android_playground.R
import com.example.android_playground.data.WorkoutSetDetails

class WorkoutHistoryDetailsAdapter :
    RecyclerView.Adapter<WorkoutHistoryDetailsAdapter.HistoryDetailsViewHolder>() {

    private var sets: List<WorkoutSetDetails> =
        emptyList()


    class HistoryDetailsViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        val exerciseName: TextView =
            itemView.findViewById(
                R.id.historyExerciseName
            )

        val setDetails: TextView =
            itemView.findViewById(
                R.id.historySetDetails
            )
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HistoryDetailsViewHolder {

        val view =
            LayoutInflater
                .from(parent.context)
                .inflate(
                    R.layout.item_workout_history_set,
                    parent,
                    false
                )

        return HistoryDetailsViewHolder(view)
    }


    override fun onBindViewHolder(
        holder: HistoryDetailsViewHolder,
        position: Int
    ) {

        val workoutSet =
            sets[position]

        holder.exerciseName.text =
            workoutSet.exerciseName

        holder.setDetails.text =
            "Set ${workoutSet.setNumber} - ${workoutSet.repetitions} reps"
    }


    override fun getItemCount(): Int {
        return sets.size
    }


    fun submitList(
        newSets: List<WorkoutSetDetails>
    ) {
        sets = newSets
        notifyDataSetChanged()
    }
}