package com.example.samsungschoolproject.view_adapter.workout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.samsungschoolproject.R;
import com.example.samsungschoolproject.database.model.PlannedWorkout;
import com.example.samsungschoolproject.databinding.WorkoutItemBinding;
import com.example.samsungschoolproject.utils.WorkoutListUtils;

import java.util.List;

public class WorkoutListAdapter extends RecyclerView.Adapter<WorkoutListAdapter.WorkoutViewHolder> {
    private final List<PlannedWorkout> plannedWorkouts;
    private final UpdateRecycler updateRecycler;
    private final OnWorkoutItemListener onWorkoutItemListener;

    public WorkoutListAdapter(List<PlannedWorkout> items, OnWorkoutItemListener onWorkoutItemListener, UpdateRecycler updateRecycler) {
        plannedWorkouts = items;
        this.onWorkoutItemListener = onWorkoutItemListener;
        this.updateRecycler = updateRecycler;
    }

    @NonNull
    @Override
    public WorkoutViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.workout_item,
                parent,
                false
        );
        return new WorkoutViewHolder(view, onWorkoutItemListener);
    }

    @Override
    public void onBindViewHolder(final WorkoutViewHolder holder, int position) {
        holder.bind(plannedWorkouts.get(position));
    }

    @Override
    public int getItemCount() {
        return plannedWorkouts.size();
    }

    public List<PlannedWorkout> getPlannedWorkouts(){
        return plannedWorkouts;
    }

    public void update(){
        updateRecycler.updateRecycler();
    }

    public interface OnWorkoutItemListener {
        void onWorkoutItemClick(int position);
    }

    public interface UpdateRecycler{
        void updateRecycler();
    }

    public static class WorkoutViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final WorkoutItemBinding workoutItemBinding;
        private final OnWorkoutItemListener onWorkoutItemListener;

        public WorkoutViewHolder(@NonNull View itemView, OnWorkoutItemListener onWorkoutItemListener) {
            super(itemView);
            workoutItemBinding = WorkoutItemBinding.bind(itemView);
            this.onWorkoutItemListener = onWorkoutItemListener;
        }

        public void bind(PlannedWorkout plannedWorkout){
            String fieldText = "Название: " + plannedWorkout.name;
            workoutItemBinding.name.setText(fieldText);
            workoutItemBinding.approximateLength.setText(WorkoutListUtils.configureWorkoutLengthInfo(plannedWorkout.approximate_length));
        }

        @Override
        public void onClick(View v) {
            onWorkoutItemListener.onWorkoutItemClick(getAdapterPosition());
        }
    }
}