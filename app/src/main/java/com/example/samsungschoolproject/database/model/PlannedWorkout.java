package com.example.samsungschoolproject.database.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "planned_workouts",
        foreignKeys = @ForeignKey(
                entity = Workout.class,
                parentColumns = "id",
                childColumns = "workout_id",
                onDelete = ForeignKey.CASCADE))
public class PlannedWorkout {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int workout_id;
    public String is_completed;
    public String date;

    public PlannedWorkout(int workout_id, String is_completed, String date) {
        this.workout_id = workout_id;
        this.is_completed = is_completed;
        this.date = date;
    }
}
