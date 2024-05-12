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
    private List<PlannedWorkout> plannedWorkouts;
    private final UpdateRecycler updateRecycler;
    private final OnWorkoutItemListener onWorkoutItemListener;
    private final SetWorkoutMarked setWorkoutMarked;
    private final DeleteWorkoutListener deleteWorkoutListener;

    public WorkoutListAdapter(
            List<PlannedWorkout> items,
            OnWorkoutItemListener onWorkoutItemListener,
            UpdateRecycler updateRecycler,
            SetWorkoutMarked setWorkoutMarked,
            DeleteWorkoutListener deleteWorkoutListener) {
        plannedWorkouts = items;
        this.onWorkoutItemListener = onWorkoutItemListener;
        this.updateRecycler = updateRecycler;
        this.setWorkoutMarked = setWorkoutMarked;
        this.deleteWorkoutListener = deleteWorkoutListener;
    }

    @NonNull
    @Override
    public WorkoutViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.workout_item,
                parent,
                false
        );
        return new WorkoutViewHolder(view, onWorkoutItemListener, setWorkoutMarked, deleteWorkoutListener);
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
        void setWorkoutMarked(PlannedWorkout plannedWorkout);
    }

    public void removeWorkoutByPosition(int position){
        plannedWorkouts.remove(position);
    }

    public interface DeleteWorkoutListener {
        void onDeleteButtonClick(int position);
    }

    public static class WorkoutViewHolder extends RecyclerView.ViewHolder{
        private final View itemView;
        private final WorkoutItemBinding workoutItemBinding;
        private final OnWorkoutItemListener onWorkoutItemListener;
        private final SetWorkoutMarked setWorkoutMarked;
        private final DeleteWorkoutListener deleteWorkoutListener;

        public WorkoutViewHolder(@NonNull View itemView,
                                 OnWorkoutItemListener onWorkoutItemListener,
                                 SetWorkoutMarked setWorkoutMarked,
                                 DeleteWorkoutListener deleteWorkoutListener) {
            super(itemView);
            workoutItemBinding = WorkoutItemBinding.bind(itemView);

            this.itemView = itemView;
            this.onWorkoutItemListener = onWorkoutItemListener;
            this.setWorkoutMarked = setWorkoutMarked;
            this.deleteWorkoutListener = deleteWorkoutListener;
        }

        public void bind(PlannedWorkout plannedWorkout){
            // Установить тенировке название
            String fieldText = "Название: " + plannedWorkout.name;
            workoutItemBinding.name.setText(fieldText);
            workoutItemBinding.approximateLength.setText(WorkoutListUtils.configureWorkoutLengthInfo(plannedWorkout.approximate_length));

            // Если тренировка "выполнена", нужно сразу установить соответствующее состояние
            if (plannedWorkout.is_completed.equals("true")){
                workoutItemBinding.markCompleted.setText(itemView.getContext().getString(R.string.completed));
                workoutItemBinding.markCompleted.setBackgroundColor(itemView.getContext().getColor(R.color.additionalButtonsColor));
            }

            initOnClickListeners(plannedWorkout);
        }

        private void initOnClickListeners(PlannedWorkout plannedWorkout){
            itemView.setOnClickListener(v -> onWorkoutItemListener.onWorkoutItemClick(getAdapterPosition()));

            // Если тренировка не "выполнена", то ее можно "выполнить" (т.е. нужно создать слушатель на кнопку
            if (plannedWorkout.is_completed.equals("false")){
                Button markCompletedButton = itemView.findViewById(R.id.markCompleted);
                markCompletedButton.setOnClickListener(v -> {
                    // Изменить текст на "выполненный" и заменить цвет кнопки
                    markCompletedButton.setText(itemView.getContext().getString(R.string.completed));


                    setWorkoutMarked.setWorkoutMarked(plannedWorkout);
                });
            }

            itemView.findViewById(R.id.delete).setOnClickListener(v -> deleteWorkoutListener.onDeleteButtonClick(getBindingAdapterPosition()));
        }
    }
}