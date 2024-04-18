package com.example.samsungschoolproject.database.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.samsungschoolproject.database.model.PlannedWorkoutExercise;

import java.util.List;

@Dao
public interface PlannedWorkoutExerciseDAO {
    @Insert
    public void addPlannedWorkoutExercise(PlannedWorkoutExercise plannedWorkoutExercise);

    @Update
    public void updatePlannedWorkoutExercise(PlannedWorkoutExercise plannedWorkoutExercise);

    @Delete
    public void deletePlannedWorkoutExercise(PlannedWorkoutExercise plannedWorkoutExercise);


    @Query("SELECT * FROM planned_workout_exercises WHERE planned_workout_id==:planned_workoutId")
    public List<PlannedWorkoutExercise> getPlannedWorkoutExercisesByWorkoutId(int planned_workoutId);
}
