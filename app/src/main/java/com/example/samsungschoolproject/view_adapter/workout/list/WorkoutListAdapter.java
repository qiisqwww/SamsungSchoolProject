package com.example.samsungschoolproject.view_adapter.workout.list;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.samsungschoolproject.R;
import com.example.samsungschoolproject.database.model.PlannedWorkout;
import com.example.samsungschoolproject.databinding.WorkoutItemBinding;
import com.example.samsungschoolproject.utils.WorkoutListUtils;

import java.util.List;
import java.util.Set;

public class WorkoutListAdapter extends RecyclerView.Adapter<WorkoutListAdapter.WorkoutViewHolder> {
    private final List<PlannedWorkout> plannedWorkouts;
    private final UpdateRecycler updateRecycler;
    private final OnWorkoutItemListener onWorkoutItemListener;
    private final SetWorkoutMarked setWorkoutMarked;

    public WorkoutListAdapter(
            List<PlannedWorkout> items,
            OnWorkoutItemListener onWorkoutItemListener,
            UpdateRecycler updateRecycler,
            SetWorkoutMarked setWorkoutMarked) {
        plannedWorkouts = items;
        this.onWorkoutItemListener = onWorkoutItemListener;
        this.updateRecycler = updateRecycler;
        this.setWorkoutMarked = setWorkoutMarked;

    }

    @NonNull
    @Override
    public WorkoutViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.workout_item,
                parent,
                false
        );
        return new WorkoutViewHolder(view, onWorkoutItemListener, setWorkoutMarked);
    }

    @Override
    public void onBindViewHolder(final WorkoutViewHolder holder, int position) {
        holder.bind(plannedWorkouts.get(position));
    }

    @Override
    public int getItemCount() {
        return plannedWorkouts.size();
    }

    public void update(){
        updateRecycler.updateRecycler();
    }

    public PlannedWorkout getItemByPosition(int position){
        return plannedWorkouts.get(position);
    }

    public interface OnWorkoutItemListener {
        void onWorkoutItemClick(int position);
    }

    public interface UpdateRecycler{
        void updateRecycler();
    }

    public interface SetWorkoutMarked{
        void setWorkoutMarked();
    }

    public static class WorkoutViewHolder extends RecyclerView.ViewHolder{
        private final View itemView;
        private final WorkoutItemBinding workoutItemBinding;
        private final OnWorkoutItemListener onWorkoutItemListener;
        private final SetWorkoutMarked setWorkoutMarked;

        public WorkoutViewHolder(@NonNull View itemView, OnWorkoutItemListener onWorkoutItemListener, SetWorkoutMarked setWorkoutMarked) {
            super(itemView);
            workoutItemBinding = WorkoutItemBinding.bind(itemView);

            this.itemView = itemView;
            this.onWorkoutItemListener = onWorkoutItemListener;
            this.setWorkoutMarked = setWorkoutMarked;
        }

        public void bind(PlannedWorkout plannedWorkout){
            String fieldText = "Название: " + plannedWorkout.name;
            workoutItemBinding.name.setText(fieldText);
            workoutItemBinding.approximateLength.setText(WorkoutListUtils.configureWorkoutLengthInfo(plannedWorkout.approximate_length));

            if (plannedWorkout.is_completed.equals("true")){
                workoutItemBinding.markCompleted.setText(itemView.getContext().getString(R.string.completed));
                workoutItemBinding.markCompleted.setBackgroundColor(itemView.getContext().getColor(R.color.additionalButtonsColor));
            }

            initOnClickListeners(plannedWorkout);
        }

        private void initOnClickListeners(PlannedWorkout plannedWorkout){
            itemView.setOnClickListener(v -> onWorkoutItemListener.onWorkoutItemClick(getAdapterPosition()));

            if (plannedWorkout.is_completed.equals("false")){
                Button markCompletedButton = itemView.findViewById(R.id.markCompleted);
                markCompletedButton.setOnClickListener(v -> { // TODO: Подумать над цветом и добавить логику подгрузки из БД
                    markCompletedButton.setText(itemView.getContext().getString(R.string.completed));
                    markCompletedButton.setBackgroundColor(itemView.getContext().getColor(R.color.additionalButtonsColor));

                    setWorkoutMarked.setWorkoutMarked();
                });
            }

        }
    }
}