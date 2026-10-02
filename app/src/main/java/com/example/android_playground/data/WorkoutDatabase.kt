package com.example.android_playground.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        Workout::class,
        Exercise::class,
        WorkoutExerciseCrossRef::class],

    version = 4
)
abstract class WorkoutDatabase : RoomDatabase(){

    // The function for getting the dao object to get the only instance of the database
    abstract  fun workoutDao() : WorkoutDao

    abstract fun exerciseDao() : ExerciseDao

    // Companian is a way of creating static members
    companion object{

        @Volatile // It tells the JVM to notify every thread immediately after the variable's value get changed
        private var INSTANCE : WorkoutDatabase? = null



        fun getDatabase(context: Context) : WorkoutDatabase{
            // Return the instance of the database and if it does not exist create it
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        WorkoutDatabase::class.java,
                        "workout_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance

                instance
            }
        }
    }
}