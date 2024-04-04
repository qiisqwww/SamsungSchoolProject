package com.example.samsungschoolproject.database.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "workouts")
public class Workout {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String name;
    public int approximate_length;
    public String is_template;

    public Workout(String name, int approximate_length, String is_template){
        this.name = name;
        this.approximate_length = approximate_length;
        this.is_template = is_template;
    }
}
