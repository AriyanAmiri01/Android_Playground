package com.example.android_playground.data

import android.content.Context

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

import androidx.sqlite.db.SupportSQLiteDatabase




@Database(
    entities = [
        Workout::class,
        Exercise::class,
        WorkoutExerciseCrossRef::class,
        WorkoutSession::class,
        WorkoutSet::class],


    version = 6
)
abstract class WorkoutDatabase : RoomDatabase(){

    // Dao Gettters
    abstract  fun workoutDao() : WorkoutDao
    abstract fun exerciseDao() : ExerciseDao
    abstract fun workoutSessionDao(): WorkoutSessionDao


    // Companian is a way of creating static members
    companion object {
        @Volatile // It tells the JVM to notify every thread immediately after the variable's value get changed
        private var INSTANCE: WorkoutDatabase? = null
        fun getDatabase(context: Context): WorkoutDatabase {
            return INSTANCE ?: synchronized(this) {

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WorkoutDatabase::class.java,
                    "workout_database"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(DatabaseCallback())
                    .build()

                INSTANCE = instance

                instance
            }
        }


        private class DatabaseCallback : RoomDatabase.Callback() {

            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)

                DefaultData.exercises.forEach { exercise ->

                    db.execSQL(
                        """
                INSERT INTO Exercise (name, muscleGroup, videoUrl)
                VALUES (?, ?, ?)
                """.trimIndent(),
                        arrayOf(
                            exercise.name,
                            exercise.muscleGroup,
                            exercise.videoUrl
                        )
                    )
                }
            }
        }
    }
}