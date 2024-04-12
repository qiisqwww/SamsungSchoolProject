package com.example.samsungschoolproject.database.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "exercises")
public class Exercise {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String name;
    public String muscle;

    public Exercise(String name, String muscle) {
        this.name = name;
        this.muscle = muscle;
    }
}
