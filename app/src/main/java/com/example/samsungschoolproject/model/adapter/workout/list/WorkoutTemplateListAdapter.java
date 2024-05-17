package com.example.samsungschoolproject.model.adapter.workout.list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.samsungschoolproject.R;
import com.example.samsungschoolproject.database.entity.WorkoutTemplate;
import com.example.samsungschoolproject.databinding.TemplateItemBinding;
import com.example.samsungschoolproject.model.util.WorkoutListUtils;

import java.util.List;

public class WorkoutTemplateListAdapter extends RecyclerView.Adapter<WorkoutTemplateListAdapter.WorkoutTemplateViewHolder> {

    private List<WorkoutTemplate> workoutTemplates;
    private final OnWorkoutItemListener onWorkoutItemListener;
    private final DeleteWorkoutTemplateListener deleteWorkoutTemplateListener;

    public WorkoutTemplateListAdapter(List<WorkoutTemplate> items, OnWorkoutItemListener onWorkoutItemListener, DeleteWorkoutTemplateListener deleteWorkoutTemplateListener) {
        workoutTemplates = items;
        this.onWorkoutItemListener = onWorkoutItemListener;
        this.deleteWorkoutTemplateListener = deleteWorkoutTemplateListener;
    }

    @NonNull
    @Override
    public WorkoutTemplateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.template_item,
                parent,
                false
        );
        return new WorkoutTemplateViewHolder(view, onWorkoutItemListener, deleteWorkoutTemplateListener);
    }

    @Override
    public void onBindViewHolder(final WorkoutTemplateViewHolder holder, int position) {
        holder.bind(workoutTemplates.get(position));
    }

    @Override
    public int getItemCount() {
        return workoutTemplates.size();
    }

    public WorkoutTemplate getItemByPosition(int position){
        return workoutTemplates.get(position);
    }

    public void removeTemplateByPosition(int position){
        workoutTemplates.remove(position);
        notifyItemRemoved(position);
    }

    public List<WorkoutTemplate> getTemplatesList(){
        return workoutTemplates;
    }

    public interface OnWorkoutItemListener {
        void onWorkoutItemClick(int position);
    }

    public interface DeleteWorkoutTemplateListener {
        void onDeleteButtonClick(int position);
    }

    public static class WorkoutTemplateViewHolder extends RecyclerView.ViewHolder{
        private final View itemView;
        private final TemplateItemBinding templateItemBinding;
        private final OnWorkoutItemListener onWorkoutItemListener;
        private final DeleteWorkoutTemplateListener deleteWorkoutTemplateListener;

        public WorkoutTemplateViewHolder(@NonNull View itemView, OnWorkoutItemListener onWorkoutItemListener, DeleteWorkoutTemplateListener deleteWorkoutTemplateListener) {
            super(itemView);
            this.itemView = itemView;
            this.onWorkoutItemListener = onWorkoutItemListener;
            this.deleteWorkoutTemplateListener = deleteWorkoutTemplateListener;

            templateItemBinding = TemplateItemBinding.bind(itemView);

            initOnClickListeners();
            initButtonListeners();
        }

        public void bind(WorkoutTemplate workoutTemplate){
            String fieldText = "Название: " + workoutTemplate.name;
            templateItemBinding.name.setText(fieldText);
            templateItemBinding.approximateLength.setText(WorkoutListUtils.configureWorkoutLengthInfo(workoutTemplate.approximate_length));
        }

        private void initOnClickListeners(){
            itemView.setOnClickListener(v -> onWorkoutItemListener.onWorkoutItemClick(getAdapterPosition()));
        }

        private void initButtonListeners(){
            itemView.findViewById(R.id.delete).setOnClickListener(v -> deleteWorkoutTemplateListener.onDeleteButtonClick(getBindingAdapterPosition()));
        }
    }
}