package com.example.samsungschoolproject.database.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = {
        @ForeignKey(
                entity = WorkoutTemplate.class,
                parentColumns = "id",
                childColumns = "workout_template_id",
                onDelete = ForeignKey.CASCADE),
        @ForeignKey(
                entity = Exercise.class,
                parentColumns = "id",
                childColumns = "exercise_id"
        )},
        tableName = "workout_template_exercises"
)
public class WorkoutTemplateExercise {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public int workout_template_id;

    public int exercise_id;

    public int repeats;

    public int approaches;

    public int number_in_query;

    public WorkoutTemplateExercise(int workout_template_id, int exercise_id, int approaches, int repeats, int number_in_query) {
        this.workout_template_id = workout_template_id;
        this.exercise_id = exercise_id;
        this.approaches = approaches;
        this.repeats = repeats;
        this.number_in_query = number_in_query;
    }
}
