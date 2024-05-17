package com.example.samsungschoolproject.database.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.samsungschoolproject.database.entity.WorkoutTemplateExercise;

import java.util.List;

@Dao
public interface WorkoutTemplateExerciseDAO {
    @Insert
    void addWorkoutTemplateExercise(WorkoutTemplateExercise workoutTemplateExercise);

    @Update
    void updateWorkoutTemplateExercise(WorkoutTemplateExercise workoutTemplateExercise);

    @Query("DELETE FROM workout_template_exercises WHERE workout_template_id==:workoutTemplateId")
    void deleteWorkoutTemplateExerciseByTemplateId(int workoutTemplateId);


    @Query("SELECT * FROM workout_template_exercises WHERE workout_template_id==:workoutTemplateId")
    List<WorkoutTemplateExercise> getWorkoutTemplateExercisesByWorkoutTemplateId(int workoutTemplateId);
}
