package com.example.samsungschoolproject.model.util;

import java.util.ArrayList;

public class WorkoutListUtils {
    public static String parseLengthTime(int length){
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
        return "Длительность ~ " + String.valueOf(approximate_length) + parseLengthTime(approximate_length);
    }

    public static int countWorkoutLength(ArrayList<ArrayList<String>> exercises){
        int workoutLength = 0;
        for (int i = 0; i < exercises.size(); i++){
            ArrayList<String> exercise = exercises.get(0);
            if (exercise.size() != 3){
                continue;
            }

            workoutLength += Integer.parseInt(exercise.get(1)) * Integer.parseInt(exercise.get(2)) * 4 + Integer.parseInt(exercise.get(1))*70;
        }

        return workoutLength/60;
    }
}
