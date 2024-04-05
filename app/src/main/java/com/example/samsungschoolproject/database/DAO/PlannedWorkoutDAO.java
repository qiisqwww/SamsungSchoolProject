package com.example.samsungschoolproject.database.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.samsungschoolproject.database.model.PlannedWorkout;
import com.example.samsungschoolproject.database.model.Workout;

import java.util.List;

@Dao
public interface PlannedWorkoutDAO {
    @Insert
    public void addPlannedWorkout(PlannedWorkout plannedWorkout);

    @Update
    public void updatePlannedWorkout(PlannedWorkout plannedWorkout);

    @Delete
    public void deletePlannedWorkout(PlannedWorkout plannedWorkout);

    @Query("SELECT * FROM planned_workouts WHERE date==:date")
    public List<PlannedWorkout> getPlannedWorkoutsByDate(String date);
}
