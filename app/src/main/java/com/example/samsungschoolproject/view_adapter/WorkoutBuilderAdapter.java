package com.example.samsungschoolproject.view_adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.samsungschoolproject.R;

import java.util.ArrayList;

public class WorkoutBuilderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private int length = 4;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        switch(viewType){
            case 0:
                view = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.field_input_name,
                        parent,
                        false
                );
                return new InputNameViewHolder(view);
            case 1:
                view = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.field_choose_exercise,
                        parent,
                        false
                );
                return new ChooseExerciseViewHolder(view);
            case 2:
                view = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.field_add_exercise,
                        parent,
                        false
                );
                return new AddExerciseViewHolder(view);
            case 3:
                view = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.field_save_workout,
                        parent,
                        false
                );
                return new SaveWorkoutButtonViewHolder(view);
        }

        return null;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0){
            return 0;
        }
        if (position == getItemCount()-2){
            return 2;
        }
        if (position == getItemCount()-1){
            return 3;
        }

        return 1;
    }

    public void addElement(){
        // Логика добавления элемента должна быть добавлена
        length++;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return length;
    }

    public class InputNameViewHolder extends RecyclerView.ViewHolder{

        public InputNameViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public class ChooseExerciseViewHolder extends RecyclerView.ViewHolder{

        public ChooseExerciseViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public class AddExerciseViewHolder extends RecyclerView.ViewHolder{

        public AddExerciseViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public class SaveWorkoutButtonViewHolder extends RecyclerView.ViewHolder{

        public SaveWorkoutButtonViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
