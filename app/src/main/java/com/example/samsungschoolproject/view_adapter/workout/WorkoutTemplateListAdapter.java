package com.example.samsungschoolproject.view_adapter.workout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.samsungschoolproject.DTO.WorkoutTemplateInfo;
import com.example.samsungschoolproject.R;
import com.example.samsungschoolproject.databinding.TemplateItemBinding;
import com.example.samsungschoolproject.utils.WorkoutListUtils;

import java.util.List;

public class WorkoutTemplateListAdapter extends RecyclerView.Adapter<WorkoutTemplateListAdapter.WorkoutTemplateViewHolder> {

    private final List<WorkoutTemplateInfo> workoutTemplatesInfo;
    private final OnWorkoutTemplateItemListener onWorkoutTemplateItemListener;

    public WorkoutTemplateListAdapter(List<WorkoutTemplateInfo> items, OnWorkoutTemplateItemListener onWorkoutTemplateItemListener) {
        workoutTemplatesInfo = items;
        this.onWorkoutTemplateItemListener = onWorkoutTemplateItemListener;
    }

    @NonNull
    @Override
    public WorkoutTemplateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.template_item,
                parent,
                false
        );
        return new WorkoutTemplateViewHolder(view, onWorkoutTemplateItemListener);
    }

    @Override
    public void onBindViewHolder(final WorkoutTemplateViewHolder holder, int position) {
        holder.bind(workoutTemplatesInfo.get(position));
    }

    @Override
    public int getItemCount() {
        return workoutTemplatesInfo.size();
    }

    public interface OnWorkoutTemplateItemListener {
        void onItemClick(int position);
    }

    public static class WorkoutTemplateViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final TemplateItemBinding templateItemBinding;
        private final OnWorkoutTemplateItemListener onWorkoutTemplateItemListener;

        public WorkoutTemplateViewHolder(@NonNull View itemView, OnWorkoutTemplateItemListener onWorkoutTemplateItemListener) {
            super(itemView);
            templateItemBinding = TemplateItemBinding.bind(itemView);
            this.onWorkoutTemplateItemListener = onWorkoutTemplateItemListener;
        }

        public void bind(WorkoutTemplateInfo workoutTemplateInfo){
            templateItemBinding.name.setText(workoutTemplateInfo.name);
            templateItemBinding.approximateLength.setText(WorkoutListUtils.configureWorkoutLengthInfo(workoutTemplateInfo.approximate_length));
        }

        @Override
        public void onClick(View v) {
            onWorkoutTemplateItemListener.onItemClick(getAdapterPosition());
        }
    }
}