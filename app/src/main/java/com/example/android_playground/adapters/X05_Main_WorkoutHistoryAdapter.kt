package com.example.android_playground.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android_playground.R
import com.example.android_playground.data.WorkoutSessionDetails
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class X05_Main_WorkoutHistoryAdapter(
    private val onSessionClick: (WorkoutSessionDetails) -> Unit
) : RecyclerView.Adapter<X05_Main_WorkoutHistoryAdapter.WorkoutHistoryViewHolder>() {

    private var sessions: List<WorkoutSessionDetails> =
        emptyList()


    class WorkoutHistoryViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        val workoutName: TextView =
            itemView.findViewById(
                R.id.historyWorkoutName
            )

        val workoutDate: TextView =
            itemView.findViewById(
                R.id.historyWorkoutDate
            )

        val workoutDuration: TextView =
            itemView.findViewById(
                R.id.historyWorkoutDuration
            )
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WorkoutHistoryViewHolder {

        val view = LayoutInflater
            .from(parent.context)
            .inflate(
                R.layout.x05_main_item_workout_history,
                parent,
                false
            )

        return WorkoutHistoryViewHolder(view)
    }


    override fun onBindViewHolder(
        holder: WorkoutHistoryViewHolder,
        position: Int
    ) {

        val session = sessions[position]

        holder.workoutName.text =
            session.workoutName


        // Convert timestamp into a readable date.
        val dateFormat =
            SimpleDateFormat(
                "dd MMM yyyy - HH:mm",
                Locale.getDefault()
            )

        holder.workoutDate.text =
            dateFormat.format(
                Date(session.startedAt)
            )


        // Calculate actual workout duration.
        val durationMilliseconds =
            session.completedAt -
                    session.startedAt

        val durationMinutes =
            durationMilliseconds / 1000 / 60

        holder.workoutDuration.text =
            "Duration: $durationMinutes min"


        holder.itemView.setOnClickListener {
            onSessionClick(session)
        }
    }


    override fun getItemCount(): Int {
        return sessions.size
    }


    fun submitList(
        newSessions: List<WorkoutSessionDetails>
    ) {
        sessions = newSessions
        notifyDataSetChanged()
    }
}