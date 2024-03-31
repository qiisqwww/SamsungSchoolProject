package com.example.samsungschoolproject.database.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "workouts")
public class Workout {
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "name")
    public String name;
    @ColumnInfo(name = "date")
    public String date;
    @ColumnInfo(name = "approximate_length")
    public int approximate_length;
    @ColumnInfo(name = "is_completed")
    public String is_completed;

    public Workout(String name, String date, int approximate_length, String is_completed){
        this.name = name;
        this.date = date;
        this.approximate_length = approximate_length;
        this.is_completed = is_completed;
    }
}
