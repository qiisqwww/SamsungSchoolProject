package com.example.samsungschoolproject.view_adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.samsungschoolproject.DTO.WorkoutInfo;
import com.example.samsungschoolproject.R;
import com.example.samsungschoolproject.databinding.WorkoutItemBinding;
import com.example.samsungschoolproject.database.model.Workout;

import java.util.List;

public class WorkoutListAdapter extends RecyclerView.Adapter<WorkoutListAdapter.WorkoutViewHolder> {

    private final List<WorkoutInfo> workoutsInfo;

    public WorkoutListAdapter(List<WorkoutInfo> items) {
        workoutsInfo = items;
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
        holder.bind(workoutsInfo.get(position));
    }

    @Override
    public int getItemCount() {
        return workoutsInfo.size();
    }

    public class WorkoutViewHolder extends RecyclerView.ViewHolder {
        private WorkoutItemBinding workoutItemBinding;

        public WorkoutViewHolder(@NonNull View itemView) {
            super(itemView);
            workoutItemBinding = WorkoutItemBinding.bind(itemView);
        }

        public void bind(WorkoutInfo workoutInfo){
            workoutItemBinding.name.setText(workoutInfo.name);
            workoutItemBinding.approximateLength.setText("~ " + workoutInfo.approximate_length + "m");
        }
    }

    public void Add(WorkoutInfo workoutInfo){
        workoutsInfo.add(workoutInfo);
        notifyDataSetChanged();
    }
}