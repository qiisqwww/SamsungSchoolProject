package com.example.samsungschoolproject.DTO;

import com.example.samsungschoolproject.database.model.Exercise;
import com.example.samsungschoolproject.database.model.PlannedWorkoutExercise;

public class ExerciseInfo {
    public String name;
    public int approaches;
    public int repeats;

    public ExerciseInfo (Exercise exercise, PlannedWorkoutExercise plannedWorkoutExercise){
        this.name = exercise.name;
        this.approaches = plannedWorkoutExercise.approaches;
        this.repeats = plannedWorkoutExercise.repeats;
    }
}
