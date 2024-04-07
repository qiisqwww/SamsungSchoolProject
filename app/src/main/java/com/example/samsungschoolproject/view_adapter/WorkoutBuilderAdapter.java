package com.example.samsungschoolproject.view_adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.samsungschoolproject.R;

public class WorkoutBuilderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
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
                return new InputNameViewHolder(view);
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
                return new InputNameViewHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
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
