package com.example.samsungschoolproject.utils;

import com.example.samsungschoolproject.database.WorkoutHelperDatabase;
import com.example.samsungschoolproject.database.model.PlannedWorkout;
import com.example.samsungschoolproject.database.model.WorkoutTemplate;

import java.util.ArrayList;
import java.util.List;

public class WorkoutListUtils {
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
        return "~ " + String.valueOf(approximate_length) + parseLengthTime(approximate_length);
    }

    public static int countWorkoutLength(ArrayList<ArrayList<String>> exercises){
        int workoutLength = 0;
        for (int i = 0; i < exercises.size(); i++){
            ArrayList<String> exercise = exercises.get(0);
            if (exercise.size() != 3){
                continue;
            }

            workoutLength += Integer.valueOf(exercise.get(1)) * Integer.valueOf(exercise.get(2)) * 2 + Integer.valueOf(exercise.get(1))*70;
        }

        return workoutLength/60;
    }
}
