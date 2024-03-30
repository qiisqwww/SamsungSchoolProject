package com.example.samsungschoolproject.model;

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
        )}
)
public class WorkoutExercises {
    @PrimaryKey public int id;
    public int workout_id;
    public int exercise_id;
    public int repeats;
    public int approaches;

    public WorkoutExercises(int id, int workout_id, int exercise_id, int repeats, int approaches) {
        this.id = id;
        this.workout_id = workout_id;
        this.exercise_id = exercise_id;
        this.repeats = repeats;
        this.approaches = approaches;
    }
}
