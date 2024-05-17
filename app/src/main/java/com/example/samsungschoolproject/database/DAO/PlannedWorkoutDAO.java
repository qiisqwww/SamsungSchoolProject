package com.example.samsungschoolproject.database.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.samsungschoolproject.database.entity.PlannedWorkout;

import java.util.List;

@Dao
public interface PlannedWorkoutDAO {
    @Insert
    long addPlannedWorkout(PlannedWorkout plannedWorkout);

    @Update
    void updatePlannedWorkout(PlannedWorkout plannedWorkout);

    @Delete
    void deletePlannedWorkout(PlannedWorkout plannedWorkout);

    @Query("SELECT * FROM planned_workouts WHERE date==:date")
    List<PlannedWorkout> getPlannedWorkoutsByDate(String date);

    @Query("SELECT * FROM planned_workouts WHERE date==:date AND name==:name")
    PlannedWorkout getPlannedWorkoutByDateAndName(String date, String name);

    @Query("SELECT COUNT(*) FROM planned_workouts")
    int getPlannedWorkoutsCount();

    @Query("SELECT COUNT(*) FROM planned_workouts WHERE is_completed==:is_completed")
    int getCompletedPlannedWorkouts(String is_completed);

    @Query("SELECT SUM(approximate_length) FROM planned_workouts")
    int getSummaryApproximateLength();
}
