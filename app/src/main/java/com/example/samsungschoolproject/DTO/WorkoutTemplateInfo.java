package com.example.samsungschoolproject.DTO;

import com.example.samsungschoolproject.database.model.PlannedWorkout;
import com.example.samsungschoolproject.database.model.Workout;
import com.example.samsungschoolproject.database.model.WorkoutTemplate;

public class WorkoutTemplateInfo {
    public int id;
    public String name;
    public String date;
    public int approximate_length;
    public WorkoutTemplateInfo(WorkoutTemplate workoutTemplate){
        id = workoutTemplate.id;
        name = workoutTemplate.name;
        approximate_length = workoutTemplate.approximate_length;
    }

    public static WorkoutTemplateInfo fromMapper (WorkoutTemplate workoutTemplate){
        return new WorkoutTemplateInfo(workoutTemplate);
    }
}
