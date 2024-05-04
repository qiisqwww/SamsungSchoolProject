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
    void addPlannedWorkoutExercise(PlannedWorkoutExercise plannedWorkoutExercise);

    @Update
    void updatePlannedWorkoutExercise(PlannedWorkoutExercise plannedWorkoutExercise);

    @Delete
    void deletePlannedWorkoutExercise(PlannedWorkoutExercise plannedWorkoutExercise);


    @Query("SELECT * FROM planned_workout_exercises WHERE planned_workout_id==:planned_workoutId")
    List<PlannedWorkoutExercise> getPlannedWorkoutExercisesByWorkoutId(int planned_workoutId);
}
