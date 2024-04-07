package com.example.samsungschoolproject.utils;

import com.example.samsungschoolproject.R;

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
        return R.string.workout_length + String.valueOf(approximate_length) + parseLengthTime(approximate_length);
    }
}
