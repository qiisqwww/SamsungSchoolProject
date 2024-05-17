package com.example.samsungschoolproject.database.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "planned_workouts")
public class PlannedWorkout {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String name;
    public int approximate_length;
    public String is_completed;
    public String date;

    public PlannedWorkout(String name, int approximate_length, String is_completed, String date) {
        this.name = name;
        this.approximate_length = approximate_length;
        this.is_completed = is_completed;
        this.date = date;
    }
}
