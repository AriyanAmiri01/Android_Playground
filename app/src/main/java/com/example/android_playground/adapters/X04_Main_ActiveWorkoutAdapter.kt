package com.example.android_playground.adapters

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android_playground.R
import com.example.android_playground.data.WorkoutExerciseDetails
import com.example.android_playground.data.WorkoutSet

class X04_Main_ActiveWorkoutAdapter : RecyclerView.Adapter<X04_Main_ActiveWorkoutAdapter.ActiveWorkoutViewHolder>() {

    // Variables
    private var exercises: List<WorkoutExerciseDetails> = emptyList()
    private val enteredRepetitions = mutableMapOf<Pair<Int, Int>, Int>()


    // 1. Wrapper class for one rercyclerView  item View
    class ActiveWorkoutViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val exerciseName: TextView = itemView.findViewById(R.id.activeExerciseName)
        val exerciseTarget: TextView = itemView.findViewById(R.id.activeExerciseTarget)
        val setsContainer: LinearLayout = itemView.findViewById(R.id.setsContainer)
    }


    // 2. inflate the xml layout into a view and wraps that inside a viewholder wrapper and returns it
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActiveWorkoutViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.x04_sub_item_active_workout_exercise, parent, false)

        return ActiveWorkoutViewHolder(view)
    }


    // 3. Fills the actual data
    override fun onBindViewHolder(holder: ActiveWorkoutViewHolder, position: Int) {
        val exercise = exercises[position]
        holder.exerciseName.text = exercise.name
        holder.exerciseTarget.text = "Target: ${exercise.sets} sets × ${exercise.repetitions} reps"
        holder.setsContainer.removeAllViews()
        for (setNumber in 1..exercise.sets) {

            // Get WorkoutSet View references
            val setView = LayoutInflater
                .from(holder.itemView.context)
                .inflate(R.layout.x04_main_item_active_workout_set, holder.setsContainer, false)
            val setNumberText = setView.findViewById<TextView>(R.id.setNumberText)
            val repetitionsInput = setView.findViewById<EditText>(R.id.repetitionsInput)
            val key = Pair(exercise.exerciseId, setNumber)
            val repetitions = enteredRepetitions[key] ?: exercise.repetitions

            // Set their values
            setNumberText.text = "Set $setNumber"
            repetitionsInput.setText(repetitions.toString())
            enteredRepetitions[key] = repetitions

            // Input event listeners
            repetitionsInput.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                    }
                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        val newRepetitions = s.toString().toIntOrNull()
                        if (newRepetitions != null) {
                            enteredRepetitions[key] =
                                newRepetitions
                        } else {
                            enteredRepetitions.remove(key)
                        }
                    }
                    override fun afterTextChanged(s: Editable?) {
                    }
                }
            )

            holder.setsContainer.addView(setView)
        }
    }


    // 4. responsable for telling the recycler view how many items are there which is used by recyclerView
    override fun getItemCount(): Int { return exercises.size }

    // 5. Sumbit callback for loading the adapter
    fun submitList(newExercises: List<WorkoutExerciseDetails>) {
        exercises = newExercises.sortedBy { it.exerciseOrder }
        notifyDataSetChanged()
    }


    // 6. Extra stuffs
    fun getWorkoutSets(sessionId: Int): List<WorkoutSet> {
        val workoutSets = mutableListOf<WorkoutSet>()
        exercises.forEach { exercise ->

            for (setNumber in 1..exercise.sets) {
                val key = Pair(exercise.exerciseId, setNumber)
                val repetitions = enteredRepetitions[key]
                if (repetitions != null) {
                    workoutSets.add(WorkoutSet(sessionId = sessionId, exerciseId = exercise.exerciseId, setNumber = setNumber, repetitions = repetitions))
                }
            }
        }
        return workoutSets
    }
}