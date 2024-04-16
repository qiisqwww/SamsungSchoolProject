package com.example.samsungschoolproject.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.samsungschoolproject.database.DAO.ExerciseDAO;
import com.example.samsungschoolproject.database.DAO.PlannedWorkoutDAO;
import com.example.samsungschoolproject.database.DAO.PlannedWorkoutExerciseDAO;
import com.example.samsungschoolproject.database.DAO.WorkoutTemplateDAO;
import com.example.samsungschoolproject.database.DAO.WorkoutTemplateExerciseDAO;
import com.example.samsungschoolproject.database.model.Exercise;
import com.example.samsungschoolproject.database.model.PlannedWorkout;
import com.example.samsungschoolproject.database.model.PlannedWorkoutExercise;
import com.example.samsungschoolproject.database.model.WorkoutTemplate;
import com.example.samsungschoolproject.database.model.WorkoutTemplateExercise;

@Database(entities = {
        PlannedWorkout.class,
        Exercise.class,
        PlannedWorkoutExercise.class,
        WorkoutTemplate.class,
        WorkoutTemplateExercise.class
}, version = 1)
public abstract class WorkoutHelperDatabase extends RoomDatabase {
    public abstract ExerciseDAO getExerciseDAO();
    public abstract PlannedWorkoutExerciseDAO getPlannedWorkoutExerciseDAO();
    public abstract PlannedWorkoutDAO getPlannedWorkoutDAO();
    public abstract WorkoutTemplateDAO getWorkoutTemplateDAO();
    public abstract WorkoutTemplateExerciseDAO getWorkoutTemplateExerciseDAO();
    public static WorkoutHelperDatabase database = null;

    public static WorkoutHelperDatabase getInstance(Context context){
        if (database == null){
            database = Room.databaseBuilder(context, WorkoutHelperDatabase.class, "workout_helper").allowMainThreadQueries().createFromAsset("database/workout_helper.db").build();
        }

        return database;
    }

}
