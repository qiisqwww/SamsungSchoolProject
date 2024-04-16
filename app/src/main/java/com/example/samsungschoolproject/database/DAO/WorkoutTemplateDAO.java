package com.example.samsungschoolproject.database.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.samsungschoolproject.database.model.WorkoutTemplate;

import java.util.List;

@Dao
public interface WorkoutTemplateDAO {
    @Insert
    public void addWorkoutTemplate(WorkoutTemplate workoutTemplate);

    @Update
    public void updateWorkoutTemplate(WorkoutTemplate workoutTemplate);

    @Delete
    public void deleteWorkoutTemplate(WorkoutTemplate workoutTemplate);

    @Query("SELECT * FROM workout_templates")
    public List<WorkoutTemplate> getAllWorkoutTemplates();

    @Query("SELECT * FROM workout_templates WHERE name==:name")
    public WorkoutTemplate getWorkoutTemplateByName(String name);

    @Query("SELECT * FROM workout_templates WHERE id==:id")
    public WorkoutTemplate getWorkoutTemplatesById(int id);
}
