package com.example.samsungschoolproject.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = {
        @ForeignKey(
                entity = Workout.class,
                parentColumns = "id",
                childColumns = "workout_id"),
        @ForeignKey(
                entity = Exercise.class,
                parentColumns = "id",
                childColumns = "exercise_id"
        )},
        tableName = "workout_exercises"
)
public class WorkoutExercise {
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "workout_id")
    public int workout_id;
    @ColumnInfo(name = "exercise_id")
    public int exercise_id;
    @ColumnInfo(name = "repeats")
    public int repeats;
    @ColumnInfo(name = "approaches")
    public int approaches;

    public WorkoutExercise(int id, int workout_id, int exercise_id, int repeats, int approaches) {
        this.id = id;
        this.workout_id = workout_id;
        this.exercise_id = exercise_id;
        this.repeats = repeats;
        this.approaches = approaches;
    }
}
