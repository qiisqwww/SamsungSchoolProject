package com.example.samsungschoolproject.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.samsungschoolproject.database.DAO.ExerciseDAO;
import com.example.samsungschoolproject.database.DAO.WorkoutDAO;
import com.example.samsungschoolproject.database.DAO.WorkoutExerciseDAO;
import com.example.samsungschoolproject.database.model.Exercise;
import com.example.samsungschoolproject.database.model.Workout;
import com.example.samsungschoolproject.database.model.WorkoutExercise;

@Database(entities = {Workout.class, Exercise.class, WorkoutExercise.class}, version = 1)
public abstract class WorkoutHelperDatabase extends RoomDatabase {
    public abstract WorkoutDAO getWorkoutDAO();
    public abstract ExerciseDAO getExerciseDAO();
    public abstract WorkoutExerciseDAO getWorkoutExerciseDAO();
    public static WorkoutHelperDatabase database = null;

    public static WorkoutHelperDatabase getInstance(Context context){
        if (database == null){
            database = Room.databaseBuilder(context, WorkoutHelperDatabase.class, "workout_helper").allowMainThreadQueries().createFromAsset("database/workout_helper.db").build();
        }

        return database;
    }

}
