package com.example.samsungschoolproject.database.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.samsungschoolproject.database.model.WorkoutTemplateExercise;

import java.util.List;

@Dao
public interface WorkoutTemplateExerciseDAO {
    @Insert
    void addWorkoutTemplateExercise(WorkoutTemplateExercise workoutTemplateExercise);

    @Update
    void updateWorkoutTemplateExercise(WorkoutTemplateExercise workoutTemplateExercise);

    @Delete
    void deleteWorkoutTemplateExercise(WorkoutTemplateExercise workoutTemplateExercise);


    @Query("SELECT * FROM workout_template_exercises WHERE workout_template_id==:workoutTemplateId")
    List<WorkoutTemplateExercise> getWorkoutTemplateExercisesByWorkoutTemplateId(int workoutTemplateId);
}
