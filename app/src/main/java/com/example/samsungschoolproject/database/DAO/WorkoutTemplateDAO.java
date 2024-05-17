package com.example.samsungschoolproject.database.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.samsungschoolproject.database.entity.WorkoutTemplate;

import java.util.List;

@Dao
public interface WorkoutTemplateDAO {
    @Insert
    long addWorkoutTemplate(WorkoutTemplate workoutTemplate);

    @Update
    void updateWorkoutTemplate(WorkoutTemplate workoutTemplate);

    @Delete
    void deleteWorkoutTemplate(WorkoutTemplate workoutTemplate);

    @Query("SELECT * FROM workout_templates")
    List<WorkoutTemplate> getAllWorkoutTemplates();

    @Query("SELECT * FROM workout_templates WHERE name==:name")
    WorkoutTemplate getWorkoutTemplateByName(String name);

    @Query("SELECT * FROM workout_templates WHERE id==:id")
    WorkoutTemplate getWorkoutTemplatesById(int id);
}
