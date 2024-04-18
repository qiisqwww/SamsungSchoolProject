package com.example.samsungschoolproject.DTO;

import com.example.samsungschoolproject.database.model.Exercise;
import com.example.samsungschoolproject.database.model.PlannedWorkoutExercise;
import com.example.samsungschoolproject.database.model.WorkoutTemplateExercise;

import java.util.ArrayList;
import java.util.List;

public class ExerciseInfo {
    public String name;
    public int approaches;
    public int repeats;

    public ExerciseInfo (Exercise exercise, PlannedWorkoutExercise plannedWorkoutExercise){
        this.name = exercise.name;
        this.approaches = plannedWorkoutExercise.approaches;
        this.repeats = plannedWorkoutExercise.repeats;
    }

    public ExerciseInfo (Exercise exercise, WorkoutTemplateExercise workoutTemplateExercise){
        this.name = exercise.name;
        this.approaches = workoutTemplateExercise.approaches;
        this.repeats = workoutTemplateExercise.repeats;
    }

    public static List<ExerciseInfo> toExerciseInfoListForPlanned(List<Exercise> exercises, List<PlannedWorkoutExercise> plannedWorkoutExercises){
        ArrayList<ExerciseInfo> exercisesInfo = new ArrayList<>();
        for (int i = 0; i < plannedWorkoutExercises.size(); i++){
            PlannedWorkoutExercise plannedWorkoutExercise = plannedWorkoutExercises.get(i);
            Exercise exercise = exercises.get(plannedWorkoutExercise.exercise_id);

            exercisesInfo.add(new ExerciseInfo(exercise, plannedWorkoutExercise));
        }

        return exercisesInfo;
    }

    public static List<ExerciseInfo> toExerciseInfoListForTemplate(List<Exercise> exercises, List<WorkoutTemplateExercise> workoutTemplateExercises){
        ArrayList<ExerciseInfo> exercisesInfo = new ArrayList<>();
        for (int i = 0; i < workoutTemplateExercises.size(); i++){
            WorkoutTemplateExercise workoutTemplateExercise = workoutTemplateExercises.get(i);
            Exercise exercise = exercises.get(workoutTemplateExercise.exercise_id);

            exercisesInfo.add(new ExerciseInfo(exercise, workoutTemplateExercise));
        }

        return exercisesInfo;
    }
}
