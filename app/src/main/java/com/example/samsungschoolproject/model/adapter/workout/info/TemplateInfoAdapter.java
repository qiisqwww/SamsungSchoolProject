package com.example.samsungschoolproject.model.adapter.workout.info;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.samsungschoolproject.model.DTO.ExerciseInfo;
import com.example.samsungschoolproject.model.DTO.TemplateInfo;
import com.example.samsungschoolproject.R;

public class TemplateInfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private TemplateInfo templateInfo;

    public TemplateInfoAdapter(TemplateInfo templateInfo){
        this.templateInfo = templateInfo;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        switch (viewType){
            case 0:
                view = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.field_info_name,
                        parent,
                        false
                );
                return new TemplateNameViewHolder(view);
            case 1:
                view = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.field_info_exercise,
                        parent,
                        false
                );
                return new TemplateExerciseViewHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (position == 0){
            TextView workoutName = holder.itemView.findViewById(R.id.workoutName);
            workoutName.setText("Название шаблона: " + templateInfo.name);
        }
        if (position != 0){
            // .get() по position-1 т.к. по 0 индекс это название тренировки
            ExerciseInfo exerciseInfo = templateInfo.exercisesInfo.get(position-1);
            TextView exerciseName = holder.itemView.findViewById(R.id.exerciseName);
            TextView approaches = holder.itemView.findViewById(R.id.approaches);
            TextView repeats = holder.itemView.findViewById(R.id.repeats);

            exerciseName.setText("Упражнение: " + exerciseInfo.name);
            approaches.setText("Количество подходов: " + exerciseInfo.approaches);
            repeats.setText("Количество повторений: " + exerciseInfo.repeats);
        }
    }

    @Override
    public int getItemCount() {
        return 1 + templateInfo.exercisesInfo.size(); // Поле для названия + количество упражнений
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0){ // field_info_name.xml
            return 0;
        }

        return 1; // field_info_exercise.xml
    }

    public static class TemplateNameViewHolder extends RecyclerView.ViewHolder{

        public TemplateNameViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public static class TemplateExerciseViewHolder extends RecyclerView.ViewHolder {
        public TemplateExerciseViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
