package com.example.samsungschoolproject.utils;

import com.example.samsungschoolproject.DTO.WorkoutInfo;
import com.example.samsungschoolproject.DTO.WorkoutTemplateInfo;
import com.example.samsungschoolproject.R;
import com.example.samsungschoolproject.database.WorkoutHelperDatabase;
import com.example.samsungschoolproject.database.model.PlannedWorkout;
import com.example.samsungschoolproject.database.model.Workout;
import com.example.samsungschoolproject.database.model.WorkoutTemplate;

import java.util.ArrayList;
import java.util.List;

public class WorkoutListUtils {
    public static String name;
    public static ArrayList<ArrayList<String>> exercises;

    private static String parseLengthTime(int length){
        if (length%100 >= 5 && length%100 <= 20){
            return " минут";
        }
        if (length%10 == 1){
            return " минута";
        }
        if (length <= 4 && length >= 2){
            return " минуты";
        }

        return " минут";
    }
    public static String configureWorkoutLengthInfo(int approximate_length){
        return R.string.workout_length + String.valueOf(approximate_length) + parseLengthTime(approximate_length);
    }

    public static ArrayList<WorkoutTemplateInfo> parseWorkoutTemplatesForAdapter(List<WorkoutTemplate> workoutTemplates){
        ArrayList<WorkoutTemplateInfo> workoutTemplatesInfo = new ArrayList<WorkoutTemplateInfo>() {
        };
        for (int i = 0; i < workoutTemplatesInfo.size(); i++) {
            WorkoutTemplate workoutTemplate = workoutTemplates.get(i);

            WorkoutTemplateInfo workoutTemplateInfo = WorkoutTemplateInfo.fromMapper(workoutTemplate);
            workoutTemplatesInfo.add(workoutTemplateInfo);
        }

        return workoutTemplatesInfo;
    }

    public static ArrayList<WorkoutInfo> parseWorkoutsForAdapter(List<PlannedWorkout> plannedWorkouts, WorkoutHelperDatabase database){
        ArrayList<WorkoutInfo> workoutsInfo = new ArrayList<WorkoutInfo>(){};
        for (int i = 0; i < plannedWorkouts.size(); i++){
            PlannedWorkout plannedWorkout = plannedWorkouts.get(i);
            Workout workout = database.getWorkoutDAO().getWorkoutById(plannedWorkout.workout_id);

            WorkoutInfo workoutInfo = WorkoutInfo.fromMapper(workout, plannedWorkout);
            workoutsInfo.add(workoutInfo);
        }

        return workoutsInfo;
    }
}
