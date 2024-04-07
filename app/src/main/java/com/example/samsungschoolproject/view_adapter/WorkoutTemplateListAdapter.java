package com.example.samsungschoolproject.view_adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.samsungschoolproject.DTO.WorkoutInfo;
import com.example.samsungschoolproject.DTO.WorkoutTemplateInfo;
import com.example.samsungschoolproject.R;
import com.example.samsungschoolproject.databinding.WorkoutItemBinding;
import com.example.samsungschoolproject.database.model.Workout;
import com.example.samsungschoolproject.utils.WorkoutListUtils;

import java.util.List;

public class WorkoutTemplateListAdapter extends RecyclerView.Adapter<WorkoutTemplateListAdapter.WorkoutTemplateViewHolder> {

    private final List<WorkoutTemplateInfo> workoutTemplatesInfo;

    public WorkoutTemplateListAdapter(List<WorkoutTemplateInfo> items) {
        workoutTemplatesInfo = items;
    }

    @NonNull
    @Override
    public WorkoutTemplateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.workout_item,
                parent,
                false
        );
        return new WorkoutTemplateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final WorkoutTemplateViewHolder holder, int position) {
        holder.bind(workoutTemplatesInfo.get(position));
    }

    @Override
    public int getItemCount() {
        return workoutTemplatesInfo.size();
    }

    public class WorkoutTemplateViewHolder extends RecyclerView.ViewHolder {
        private WorkoutItemBinding workoutTemplateItemBinding;

        public WorkoutTemplateViewHolder(@NonNull View itemView) {
            super(itemView);
            workoutTemplateItemBinding = WorkoutItemBinding.bind(itemView);
        }

        public void bind(WorkoutTemplateInfo workoutTemplateInfo){
            workoutTemplateItemBinding.name.setText(workoutTemplateInfo.name);
            workoutTemplateItemBinding.approximateLength.setText(WorkoutListUtils.configureWorkoutLengthInfo(workoutTemplateInfo.approximate_length));
        }
    }

    public void Add(WorkoutTemplateInfo workoutTemplateInfo){
        workoutTemplatesInfo.add(workoutTemplateInfo);
        notifyDataSetChanged();
    }
}