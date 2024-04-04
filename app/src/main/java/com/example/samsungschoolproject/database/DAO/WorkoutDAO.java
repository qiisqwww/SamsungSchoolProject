package com.example.samsungschoolproject.database.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.samsungschoolproject.database.model.Workout;

import java.util.List;

@Dao
public interface WorkoutDAO {
    @Insert
    public void addWorkout(Workout workout);

    @Update
    public void updateWorkout(Workout workout);

    @Delete
    public void deleteWorkout(Workout workout);

    @Query("SELECT * FROM workouts")
    public List<Workout> getAllWorkouts();

    @Query("SELECT * FROM workouts WHERE id==:id")
    public Workout getWorkoutById(int id);

    @Query("SELECT * FROM workouts WHERE is_template==:is_template")
    public List<Workout> getWorkoutTemplates(String is_template);
}
