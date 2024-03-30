package com.example.samsungschoolproject.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Workout {
    @PrimaryKey public int id;
    public String name;
    public String date;
    public int approximate_length;
    public String is_completed;

    public Workout(String name, String date, int approximate_length, String is_completed){
        this.name = name;
        this.date = date;
        this.approximate_length = approximate_length;
        this.is_completed = is_completed;
    }
}
