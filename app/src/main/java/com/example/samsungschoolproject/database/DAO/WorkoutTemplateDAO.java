package com.example.samsungschoolproject.database.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.samsungschoolproject.database.model.Workout;
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

    @Query("SELECT * FROM workouts")
    public List<WorkoutTemplate> getAllWorkoutTemplates();

    @Query("SELECT * FROM workouts WHERE id==:id")
    public WorkoutTemplate getWorkoutTemplatesById(int id);
}
