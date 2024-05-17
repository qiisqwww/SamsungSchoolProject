package com.example.samsungschoolproject.model.util;

import com.example.samsungschoolproject.database.entity.Exercise;

import java.util.ArrayList;
import java.util.List;

public class ExerciseListUtils {
    public static final String[] approaches = {"1", "2", "3", "4", "5"};
    public static final String[] repeats = {"5", "8", "10", "12", "15", "20"};
    public static List<String> parseExerciseToStrings(List<Exercise> exercises){
        ArrayList<String> stringExercises = new ArrayList<>();

        for (int i = 0; i < exercises.size(); i++){
            stringExercises.add(exercises.get(i).name);
        }

        return stringExercises;
    }
}
