package com.example.samsungschoolproject.database.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.samsungschoolproject.database.model.Exercise;

import java.util.List;

@Dao
public interface ExerciseDAO {
    @Insert
    public void addExercise(Exercise exercise);

    @Update
    public void updateExercise(Exercise exercise);

    @Delete
    public void deleteExercise(Exercise exercise);

    @Query("SELECT * FROM exercises")
    public List<Exercise> getAllExercises();

    @Query("SELECT * FROM exercises WHERE id==:exerciseId")
    public Exercise getExerciseById(int exerciseId);
}
