package com.example.samsungschoolproject.view_adapter.workout.list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.samsungschoolproject.R;
import com.example.samsungschoolproject.database.model.WorkoutTemplate;
import com.example.samsungschoolproject.databinding.TemplateForWorkoutItemBinding;
import com.example.samsungschoolproject.databinding.TemplateItemBinding;
import com.example.samsungschoolproject.utils.WorkoutListUtils;

import java.util.List;

public class TemplatesForWorkoutListAdapter extends RecyclerView.Adapter<TemplatesForWorkoutListAdapter.TemplateForWorkoutViewHolder> {

    private final List<WorkoutTemplate> workoutTemplates;
    private final OnTemplateForWorkoutItemListener onWorkoutItemListener;

    public TemplatesForWorkoutListAdapter(List<WorkoutTemplate> items, OnTemplateForWorkoutItemListener onTemplateForWorkoutItemListener) {
        workoutTemplates = items;
        this.onWorkoutItemListener = onTemplateForWorkoutItemListener;
    }

    @NonNull
    @Override
    public TemplateForWorkoutViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.template_for_workout_item,
                parent,
                false
        );
        return new TemplateForWorkoutViewHolder(view, onWorkoutItemListener);
    }

    @Override
    public void onBindViewHolder(final TemplateForWorkoutViewHolder holder, int position) {
        holder.bind(workoutTemplates.get(position));
    }

    @Override
    public int getItemCount() {
        return workoutTemplates.size();
    }

    public interface OnTemplateForWorkoutItemListener {
        void onWorkoutItemClick(int position);
    }

    public static class TemplateForWorkoutViewHolder extends RecyclerView.ViewHolder{
        private final View itemView;
        private final TemplateForWorkoutItemBinding templateForWorkoutItemBinding;
        private final OnTemplateForWorkoutItemListener onTemplateForWorkoutItemListener;

        public TemplateForWorkoutViewHolder(@NonNull View itemView, OnTemplateForWorkoutItemListener onTemplateForWorkoutItemListener) {
            super(itemView);
            this.itemView = itemView;
            this.onTemplateForWorkoutItemListener = onTemplateForWorkoutItemListener;

            templateForWorkoutItemBinding = TemplateForWorkoutItemBinding.bind(itemView);

            initOnClickListeners();
        }

        public void bind(WorkoutTemplate workoutTemplate){
            String fieldText = "Название: " + workoutTemplate.name;
            templateForWorkoutItemBinding.name.setText(fieldText);
            templateForWorkoutItemBinding.approximateLength.setText(WorkoutListUtils.configureWorkoutLengthInfo(workoutTemplate.approximate_length));
        }

        private void initOnClickListeners(){
            itemView.setOnClickListener(v -> onTemplateForWorkoutItemListener.onWorkoutItemClick(getAdapterPosition()));
        }
    }
}