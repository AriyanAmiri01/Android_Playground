package com.example.android_playground.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android_playground.R
import com.example.android_playground.data.WorkoutSetDetails


data class WorkoutHistoryExercise(val exerciseId: Int, val exerciseName: String, val sets: List<WorkoutSetDetails>)


class WorkoutHistoryDetailsAdapter : RecyclerView.Adapter<WorkoutHistoryDetailsAdapter.HistoryDetailsViewHolder>() {

    // Variables
    private var exercises: List<WorkoutHistoryExercise> = emptyList()


    // 1. Wrapper class for one rercyclerView  item View
    class HistoryDetailsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val exerciseName: TextView = itemView.findViewById(R.id.historyExerciseName)
        val setsContainer: LinearLayout = itemView.findViewById(R.id.historySetsContainer)
    }


    // 2. inflate the xml layout into a view and wraps that inside a viewholder wrapper and returns it
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryDetailsViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.x05_sub_item_workout_history_set, parent, false)
        return HistoryDetailsViewHolder(view)
    }


    // 3. Fills the actual data
    override fun onBindViewHolder(holder: HistoryDetailsViewHolder, position: Int) {
        val exercise = exercises[position]

        holder.exerciseName.text = exercise.exerciseName
        holder.setsContainer.removeAllViews()
        exercise.sets.forEach { workoutSet ->

            // Fetch item view references
            val setView = LayoutInflater
                .from(holder.itemView.context)
                .inflate(R.layout.x05_sub_item_workout_history_set_row, holder.setsContainer, false)
            val setNumber = setView.findViewById<TextView>(R.id.historySetNumber)
            val repetitions = setView.findViewById<TextView>(R.id.historySetRepetitions)

            // Assign their values
            setNumber.text = "Set ${workoutSet.setNumber}"
            repetitions.text = "${workoutSet.repetitions} reps"
            holder.setsContainer.addView(setView)
        }
    }


    // 4. responsable for telling the recycler view how many items are there which is used by recyclerView
    override fun getItemCount(): Int {
        return exercises.size
    }


    // 5. Sumbit callback for loading the adapter
    fun submitList(workoutSets: List<WorkoutSetDetails>) {
        exercises = workoutSets.groupBy { it.exerciseId }
            .map { (_, sets) ->
                WorkoutHistoryExercise(
                    exerciseId = sets.first().exerciseId,
                    exerciseName = sets.first().exerciseName,
                    sets = sets.sortedBy { it.setNumber })
                }

        notifyDataSetChanged()
    }
}