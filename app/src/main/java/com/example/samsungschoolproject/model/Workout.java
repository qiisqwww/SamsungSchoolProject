package com.example.samsungschoolproject.model;

import java.time.LocalDate;

public class Workout {
    public String name;
    public LocalDate date;
    public int approximate_length;

    public Workout(String name, LocalDate date, int approximate_length){ // Must be reworked
        this.name = name;
        this.date = date;
        this.approximate_length = approximate_length;
    }

}
