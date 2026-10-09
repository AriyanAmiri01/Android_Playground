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

class X04_Main_ActiveWorkoutAdapter :
    RecyclerView.Adapter<X04_Main_ActiveWorkoutAdapter.ActiveWorkoutViewHolder>() {

    private var exercises: List<WorkoutExerciseDetails> = emptyList()


    /*
     * Stores the repetitions entered by the user.
     *
     * Key:
     *      Pair(exerciseId, setNumber)
     *
     * Value:
     *      repetitions
     *
     * Example:
     *      (exerciseId = 4, setNumber = 2) -> 9 reps
     */
    private val enteredRepetitions =
        mutableMapOf<Pair<Int, Int>, Int>()


    class ActiveWorkoutViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        val exerciseName: TextView =
            itemView.findViewById(R.id.activeExerciseName)

        val exerciseTarget: TextView =
            itemView.findViewById(R.id.activeExerciseTarget)

        val setsContainer: LinearLayout =
            itemView.findViewById(R.id.setsContainer)
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ActiveWorkoutViewHolder {

        val view = LayoutInflater
            .from(parent.context)
            .inflate(
                R.layout.x04_sub_item_active_workout_exercise,
                parent,
                false
            )

        return ActiveWorkoutViewHolder(view)
    }


    override fun onBindViewHolder(
        holder: ActiveWorkoutViewHolder,
        position: Int
    ) {

        val exercise = exercises[position]

        holder.exerciseName.text =
            exercise.name

        holder.exerciseTarget.text =
            "Target: ${exercise.sets} sets × ${exercise.repetitions} reps"


        /*
         * RecyclerView reuses ViewHolders.
         *
         * Therefore remove any set rows that may have belonged
         * to another exercise before rebuilding them.
         */
        holder.setsContainer.removeAllViews()


        for (setNumber in 1..exercise.sets) {

            val setView = LayoutInflater
                .from(holder.itemView.context)
                .inflate(
                    R.layout.x04_main_item_active_workout_set,
                    holder.setsContainer,
                    false
                )


            val setNumberText =
                setView.findViewById<TextView>(
                    R.id.setNumberText
                )

            val repetitionsInput =
                setView.findViewById<EditText>(
                    R.id.repetitionsInput
                )


            setNumberText.text =
                "Set $setNumber"


            val key =
                Pair(
                    exercise.exerciseId,
                    setNumber
                )


            /*
             * If the user has already changed this value,
             * show their value.
             *
             * Otherwise use the planned repetitions.
             */
            val repetitions =
                enteredRepetitions[key]
                    ?: exercise.repetitions


            repetitionsInput.setText(
                repetitions.toString()
            )


            /*
             * Store the initial value too.
             */
            enteredRepetitions[key] =
                repetitions


            repetitionsInput.addTextChangedListener(
                object : TextWatcher {

                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                    }


                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {

                        val newRepetitions =
                            s.toString().toIntOrNull()

                        if (newRepetitions != null) {
                            enteredRepetitions[key] =
                                newRepetitions
                        } else {
                            enteredRepetitions.remove(key)
                        }
                    }


                    override fun afterTextChanged(
                        s: Editable?
                    ) {
                    }
                }
            )


            holder.setsContainer.addView(setView)
        }
    }


    override fun getItemCount(): Int {
        return exercises.size
    }


    fun submitList(
        newExercises: List<WorkoutExerciseDetails>
    ) {

        exercises =
            newExercises.sortedBy {
                it.exerciseOrder
            }

        notifyDataSetChanged()
    }


    /*
     * Converts everything entered by the user into
     * WorkoutSet objects ready to be stored in Room.
     */
    fun getWorkoutSets(
        sessionId: Int
    ): List<WorkoutSet> {

        val workoutSets =
            mutableListOf<WorkoutSet>()


        exercises.forEach { exercise ->

            for (setNumber in 1..exercise.sets) {

                val key =
                    Pair(
                        exercise.exerciseId,
                        setNumber
                    )


                val repetitions =
                    enteredRepetitions[key]


                /*
                 * Only save the set if it contains
                 * a valid repetitions value.
                 */
                if (repetitions != null) {

                    workoutSets.add(
                        WorkoutSet(
                            sessionId = sessionId,
                            exerciseId = exercise.exerciseId,
                            setNumber = setNumber,
                            repetitions = repetitions
                        )
                    )
                }
            }
        }


        return workoutSets
    }
}