package com.example.android_playground.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android_playground.R
import com.example.android_playground.data.WorkoutSetDetails


data class WorkoutHistoryExercise(
    val exerciseId: Int,
    val exerciseName: String,
    val sets: List<WorkoutSetDetails>
)


class WorkoutHistoryDetailsAdapter :
    RecyclerView.Adapter<WorkoutHistoryDetailsAdapter.HistoryDetailsViewHolder>() {


    private var exercises: List<WorkoutHistoryExercise> =
        emptyList()


    class HistoryDetailsViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        val exerciseName: TextView =
            itemView.findViewById(
                R.id.historyExerciseName
            )

        val setsContainer: LinearLayout =
            itemView.findViewById(
                R.id.historySetsContainer
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
                    R.layout.x05_sub_item_workout_history_set,
                    parent,
                    false
                )

        return HistoryDetailsViewHolder(view)
    }


    override fun onBindViewHolder(
        holder: HistoryDetailsViewHolder,
        position: Int
    ) {

        val exercise =
            exercises[position]


        // Exercise name
        holder.exerciseName.text =
            exercise.exerciseName


        /*
         * RecyclerView reuses ViewHolders,
         * so remove rows belonging to the
         * previously displayed exercise.
         */
        holder.setsContainer.removeAllViews()


        // Create one row for each performed set.
        exercise.sets.forEach { workoutSet ->

            val setView =
                LayoutInflater
                    .from(holder.itemView.context)
                    .inflate(
                        R.layout.x05_sub_item_workout_history_set_row,
                        holder.setsContainer,
                        false
                    )


            val setNumber =
                setView.findViewById<TextView>(
                    R.id.historySetNumber
                )

            val repetitions =
                setView.findViewById<TextView>(
                    R.id.historySetRepetitions
                )


            setNumber.text =
                "Set ${workoutSet.setNumber}"

            repetitions.text =
                "${workoutSet.repetitions} reps"


            holder.setsContainer.addView(
                setView
            )
        }
    }


    override fun getItemCount(): Int {
        return exercises.size
    }


    fun submitList(
        workoutSets: List<WorkoutSetDetails>
    ) {

        /*
         * Group every set belonging to the
         * same exercise.
         */
        exercises =
            workoutSets
                .groupBy {
                    it.exerciseId
                }
                .map { (_, sets) ->

                    WorkoutHistoryExercise(
                        exerciseId =
                            sets.first().exerciseId,

                        exerciseName =
                            sets.first().exerciseName,

                        sets =
                            sets.sortedBy {
                                it.setNumber
                            }
                    )
                }


        notifyDataSetChanged()
    }
}