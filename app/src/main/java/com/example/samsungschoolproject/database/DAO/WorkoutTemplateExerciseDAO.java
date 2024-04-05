package com.example.samsungschoolproject.database.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.samsungschoolproject.database.model.WorkoutExercise;
import com.example.samsungschoolproject.database.model.WorkoutTemplateExercise;

@Dao
public interface WorkoutTemplateExerciseDAO {
    @Insert
    public void addWorkoutTemplateExercise(WorkoutTemplateExercise workoutTemplateExercise);

    @Update
    public void updateWorkoutTemplateExercise(WorkoutTemplateExercise workoutTemplateExercise);

    @Delete
    public void deleteWorkoutTemplateExercise(WorkoutTemplateExercise workoutTemplateExercise);


    @Query("SELECT * FROM workout_template_exercises WHERE workout_template_id==:workoutTemplateId")
    public WorkoutTemplateExercise getWorkoutTemplateExercisesByWorkoutId(int workoutTemplateId);
}
