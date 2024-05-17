package com.example.samsungschoolproject.model.DTO;

import com.example.samsungschoolproject.database.entity.WorkoutTemplate;

import java.util.List;

public class TemplateInfo {
    public String name;
    public List<ExerciseInfo> exercisesInfo;
    public int approximateLength;

    public TemplateInfo(WorkoutTemplate workoutTemplate, List<ExerciseInfo> exercisesInfo){
        this.name = workoutTemplate.name;
        this.exercisesInfo = exercisesInfo;
        this.approximateLength = workoutTemplate.approximate_length;
    }
}
