package com.example.samsungschoolproject.database.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = {
        @ForeignKey(
                entity = PlannedWorkout.class,
                parentColumns = "id",
                childColumns = "planned_workout_id",
                onDelete = ForeignKey.CASCADE),
        @ForeignKey(
                entity = Exercise.class,
                parentColumns = "id",
                childColumns = "exercise_id"
        )},
        tableName = "planned_workout_exercises"
)
public class PlannedWorkoutExercise {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public int planned_workout_id;

    public int exercise_id;

    public int repeats;

    public int approaches;

    public int number_in_query;

    public PlannedWorkoutExercise(int planned_workout_id, int exercise_id, int repeats, int approaches, int number_in_query) {
        this.planned_workout_id = planned_workout_id;
        this.exercise_id = exercise_id;
        this.repeats = repeats;
        this.approaches = approaches;
        this.number_in_query = number_in_query;
    }
}
