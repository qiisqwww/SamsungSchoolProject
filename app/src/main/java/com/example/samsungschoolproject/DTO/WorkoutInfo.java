package com.example.samsungschoolproject.DTO;

import com.example.samsungschoolproject.database.model.PlannedWorkout;
import com.example.samsungschoolproject.database.model.Workout;

public class WorkoutInfo {
    public int id;
    public String name;
    public String date;
    public int approximate_length;
    public String is_template;
    public String is_completed;

    public WorkoutInfo(Workout workout, PlannedWorkout plannedWorkout){
        id = workout.id;
        name = workout.name;
        date = plannedWorkout.date;
        approximate_length = workout.approximate_length;
        is_template = workout.is_template;
        is_completed = plannedWorkout.is_completed;
    }

    public static WorkoutInfo fromMapper (Workout workout, PlannedWorkout plannedWorkout){
        return new WorkoutInfo(workout, plannedWorkout);
    }
}
