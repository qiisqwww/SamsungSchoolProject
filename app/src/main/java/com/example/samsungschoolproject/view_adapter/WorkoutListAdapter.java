package com.example.samsungschoolproject.view_adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.samsungschoolproject.R;
import com.example.samsungschoolproject.databinding.WorkoutItemBinding;
import com.example.samsungschoolproject.model.Training;

import java.util.List;

public class WorkoutListAdapter extends RecyclerView.Adapter<WorkoutListAdapter.WorkoutViewHolder> {

    private final List<Training> trainings;

    public WorkoutListAdapter(List<Training> items) {
        trainings = items;
    }

    @NonNull
    @Override
    public WorkoutViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.workout_item,
                parent,
                false
        );
        return new WorkoutViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final WorkoutViewHolder holder, int position) {
        holder.bind(trainings.get(position));
    }

    @Override
    public int getItemCount() {
        return trainings.size();
    }

    public class WorkoutViewHolder extends RecyclerView.ViewHolder {
        private WorkoutItemBinding workoutItemBinding;

        public WorkoutViewHolder(@NonNull View itemView) {
            super(itemView);
            workoutItemBinding = WorkoutItemBinding.bind(itemView);
        }

        public void bind(Training training){
            workoutItemBinding.name.setText(training.name);
            workoutItemBinding.line.setText(training.line);
            workoutItemBinding.color.setText(training.color);
            // MUST BE ADDED A LOGIC !!! (cuz now idk how to make it correctly lol)
        }
    }

    public void Add(Training training){
        trainings.add(training);
        notifyDataSetChanged();
    }
}