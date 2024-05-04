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
    void addExercise(Exercise exercise);

    @Update
    void updateExercise(Exercise exercise);

    @Delete
    void deleteExercise(Exercise exercise);

    @Query("SELECT * FROM exercises")
    List<Exercise> getAllExercises();

    @Query("SELECT * FROM exercises WHERE id==:exerciseId")
    Exercise getExerciseById(int exerciseId);

    @Query("SELECT * FROM exercises WHERE name==:name")
    Exercise getExerciseByName(String name);
}
