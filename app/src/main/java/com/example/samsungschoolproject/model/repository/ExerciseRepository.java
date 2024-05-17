package com.example.samsungschoolproject.model.repository;

import com.example.samsungschoolproject.database.WorkoutHelperDatabase;
import com.example.samsungschoolproject.database.entity.Exercise;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class ExerciseRepository {
    WorkoutHelperDatabase database;

    public ExerciseRepository (WorkoutHelperDatabase database){
        this.database = database;
    }

    public List<Exercise> getAllExercises(){
        CompletableFuture<List<Exercise>> future = CompletableFuture.supplyAsync(() -> database.getExerciseDAO().getAllExercises());
        try {
            return future.get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
