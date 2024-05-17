package com.example.samsungschoolproject.DTO;

import com.example.samsungschoolproject.database.entity.PlannedWorkout;

import java.util.List;

public class WorkoutInfo {
    public String name;
    public List<ExerciseInfo> exercisesInfo;
    public String date;
    public int approximateLength;
    public String isCompleted;

    public WorkoutInfo(PlannedWorkout plannedWorkout, List<ExerciseInfo> exercisesInfo){
        this.name = plannedWorkout.name;
        this.exercisesInfo = exercisesInfo;
        this.date = plannedWorkout.date;
        this.approximateLength = plannedWorkout.approximate_length;
        this.isCompleted = plannedWorkout.is_completed;
    }
}
