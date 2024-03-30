package com.example.samsungschoolproject.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Exercise {
    @PrimaryKey public int id;
    public String name;
    public String muscle;

    public Exercise(int id, String name, String muscle) {
        this.id = id;
        this.name = name;
        this.muscle = muscle;
    }
}
