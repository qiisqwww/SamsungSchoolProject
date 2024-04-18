package com.example.samsungschoolproject.view_adapter.workout.info;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.samsungschoolproject.database.model.PlannedWorkout;

import java.util.List;

public class WorkoutInfoAdapter extends RecyclerView.Adapter {
    private List<PlannedWorkout> plannedWorkouts;

    public WorkoutInfoAdapter(List<PlannedWorkout> plannedWorkouts){
        this.plannedWorkouts = plannedWorkouts;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
