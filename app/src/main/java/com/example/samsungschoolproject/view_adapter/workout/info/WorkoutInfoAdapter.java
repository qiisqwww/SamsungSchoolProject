package com.example.samsungschoolproject.view_adapter.workout.info;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.samsungschoolproject.DTO.WorkoutInfo;
import com.example.samsungschoolproject.database.model.PlannedWorkout;

import java.util.List;

public class WorkoutInfoAdapter extends RecyclerView.Adapter {
    private WorkoutInfo workoutInfo;

    public WorkoutInfoAdapter(WorkoutInfo workoutInfo){
        this.workoutInfo = workoutInfo;
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
        return 1 + workoutInfo.exercisesInfo.size(); // Поле для названия + количество упражнений
    }

    public static class PlannedWorkoutViewHolder extends RecyclerView.ViewHolder{

        public PlannedWorkoutViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
