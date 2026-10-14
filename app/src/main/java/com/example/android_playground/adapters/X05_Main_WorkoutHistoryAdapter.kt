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

class X05_Main_WorkoutHistoryAdapter(private val onSessionClick: (WorkoutSessionDetails) -> Unit) : RecyclerView.Adapter<X05_Main_WorkoutHistoryAdapter.WorkoutHistoryViewHolder>() {


    // Variables
    private var sessions: List<WorkoutSessionDetails> = emptyList()

    // 1. Wrapper class for one rercyclerView  item View
    class WorkoutHistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val workoutName: TextView = itemView.findViewById(R.id.historyWorkoutName)
        val workoutDate: TextView = itemView.findViewById(R.id.historyWorkoutDate)
        val workoutDuration: TextView = itemView.findViewById(R.id.historyWorkoutDuration)
    }

    // 2. inflate the xml layout into a view and wraps that inside a viewholder wrapper and returns it
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutHistoryViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.x05_main_item_workout_history, parent, false)
        return WorkoutHistoryViewHolder(view)
    }

    // 3. Fills the actual data
    override fun onBindViewHolder(holder: WorkoutHistoryViewHolder, position: Int) {
        // Fetch values
        val session= sessions[position]
        val dateFormat = SimpleDateFormat("dd MMM yyyy - HH:mm", Locale.getDefault())
        val durationMilliseconds = session.completedAt - session.startedAt
        val durationMinutes = durationMilliseconds / 1000 / 60

        // Assign them to holders
        holder.workoutName.text = session.workoutName
        holder.workoutDate.text = dateFormat.format(Date(session.startedAt))
        holder.workoutDuration.text = "Duration: $durationMinutes min"
        holder.itemView.setOnClickListener { onSessionClick(session) }
    }

    // 4. responsable for telling the recycler view how many items are there which is used by recyclerView
    override fun getItemCount(): Int {
        return sessions.size
    }


    // 5. Sumbit callback for loading the adapter
    fun submitList(newSessions: List<WorkoutSessionDetails>) {
        sessions = newSessions
        notifyDataSetChanged()
    }
}