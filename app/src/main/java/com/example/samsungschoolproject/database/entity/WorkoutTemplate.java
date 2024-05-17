package com.example.samsungschoolproject.database.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "workout_templates")
public class WorkoutTemplate {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String name;
    public int approximate_length;

    public WorkoutTemplate(String name, int approximate_length){
        this.name = name;
        this.approximate_length = approximate_length;
    }
}
