package com.example.samsungschoolproject.view_adapter.workout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.samsungschoolproject.R;
import com.example.samsungschoolproject.utils.ExerciseListUtils;
import com.example.samsungschoolproject.view_adapter.exercise.SpinnerAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WorkoutBuilderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private int length = 4;

    private ArrayList<View> views;
    private final RecyclerView workoutBuilderRecycler;
    private final List<String> exercises;

    public WorkoutBuilderAdapter (List<String> exercises, RecyclerView workoutBuilderRecycler){
        this.workoutBuilderRecycler = workoutBuilderRecycler;
        this.exercises = exercises;

        views = new ArrayList<>();
    }

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
                return new InputNameViewHolder(view, this);
            case 1:
                view = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.field_choose_exercise,
                        parent,
                        false
                );
                return new ChooseExerciseViewHolder(view, this, exercises);
            case 2:
                view = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.field_add_exercise,
                        parent,
                        false
                );
                return new AddExerciseViewHolder(view, this);
            case 3:
                view = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.field_save_workout,
                        parent,
                        false
                );
                return new SaveWorkoutButtonViewHolder(view, this);
        }

        return null;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0){
            return 0; //  inputName Field
        }
        if (position == getItemCount()-2){
            return 2; //  addExercise Button
        }
        if (position == getItemCount()-1){
            return 3; //  saveWorkout Button
        }

        return 1; //  fillExercise Field
    }

    public void addView(View view){
        views.add(view);
    }

    public View getItemViewByPosition(int position){
        return views.get(position);
    }

    public void addExercise(){
        length++;
        notifyItemChanged(length-1);
        notifyItemChanged(length-2);
        notifyItemChanged(length-3);
    }

    public void deleteExercise(int position){
        length--;
        views.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return length;
    }

    public static class InputNameViewHolder extends RecyclerView.ViewHolder{
        private final View itemView;
        private final WorkoutBuilderAdapter workoutBuilderAdapter;
        public InputNameViewHolder(@NonNull View itemView, WorkoutBuilderAdapter workoutBuilderAdapter) {
            super(itemView);
            this.itemView = itemView;
            this.workoutBuilderAdapter = workoutBuilderAdapter;

            addItemViewToViewList();
        }

        private void addItemViewToViewList(){
            workoutBuilderAdapter.addView(itemView);
        }
    }

    public static class ChooseExerciseViewHolder extends RecyclerView.ViewHolder{
        private final Button deleteExerciseButton;
        private final View itemView;
        private final Spinner exerciseListSpinner, approachesListSpinner, repeatsListSpinner;

        private final List<String> exercises;
        private final WorkoutBuilderAdapter workoutBuilderAdapter;

        public ChooseExerciseViewHolder(@NonNull View itemView, WorkoutBuilderAdapter workoutBuilderAdapter, List<String> exercises) {
            super(itemView);
            this.itemView = itemView;

            this.exercises = exercises;
            this.workoutBuilderAdapter = workoutBuilderAdapter;

            deleteExerciseButton = itemView.findViewById(R.id.deleteExercise);
            exerciseListSpinner = itemView.findViewById(R.id.exerciseList);
            approachesListSpinner = itemView.findViewById(R.id.approachesList);
            repeatsListSpinner = itemView.findViewById(R.id.repeatsList);

            initButtonListeners(getAdapterPosition());
            setSpinnerAdapters();

            addItemViewToViewList();
        }

        private void initButtonListeners(int position){
            deleteExerciseButton.setOnClickListener(v -> {
                workoutBuilderAdapter.deleteExercise(position);
            });
        }

        private void setSpinnerAdapters(){
            SpinnerAdapter exerciseAdapter = new SpinnerAdapter(
                    itemView.getContext(),
                    R.layout.spinner_item_title,
                    R.layout.spinner_item_dropdown,
                    exercises);
            exerciseListSpinner.setAdapter(exerciseAdapter);

            SpinnerAdapter approachesAdapter = new SpinnerAdapter(
                    itemView.getContext(),
                    R.layout.spinner_item_title,
                    R.layout.spinner_item_dropdown,
                    Arrays.asList(ExerciseListUtils.approaches)
            );
            approachesListSpinner.setAdapter(approachesAdapter);

            SpinnerAdapter repeatsAdapter = new SpinnerAdapter(
                    itemView.getContext(),
                    R.layout.spinner_item_title,
                    R.layout.spinner_item_dropdown,
                    Arrays.asList(ExerciseListUtils.repeats)
            );
            repeatsListSpinner.setAdapter(repeatsAdapter);
        }

        private void addItemViewToViewList(){
            workoutBuilderAdapter.addView(itemView);
        }
    }

    public static class AddExerciseViewHolder extends RecyclerView.ViewHolder{
        private final View itemView;
        private final Button addExerciseButton;
        private final WorkoutBuilderAdapter workoutBuilderAdapter;

        public AddExerciseViewHolder(@NonNull View itemView, WorkoutBuilderAdapter workoutBuilderAdapter) {
            super(itemView);
            this.itemView = itemView;

            addExerciseButton = itemView.findViewById(R.id.addExercise);
            this.workoutBuilderAdapter = workoutBuilderAdapter;
            initButtonListeners();

            addItemViewToViewList();
        }

        private void initButtonListeners(){
            addExerciseButton.setOnClickListener(v -> {
                workoutBuilderAdapter.addExercise();
            });
        }

        private void addItemViewToViewList(){
            workoutBuilderAdapter.addView(itemView);
        }
    }

    public static class SaveWorkoutButtonViewHolder extends RecyclerView.ViewHolder{
        private final Button saveWorkoutButton;
        private final View itemView;
        private final WorkoutBuilderAdapter workoutBuilderAdapter;

        public SaveWorkoutButtonViewHolder(@NonNull View itemView, WorkoutBuilderAdapter workoutBuilderAdapter) {
            super(itemView);
            this.itemView = itemView;

            saveWorkoutButton = itemView.findViewById(R.id.saveWorkout);
            this.workoutBuilderAdapter = workoutBuilderAdapter;

            initButtonListeners();
            addItemViewToViewList();
        }

        private void initButtonListeners(){
            saveWorkoutButton.setOnClickListener( v -> {
                String name;
                ArrayList<ArrayList<String>> exercises = new ArrayList<>();

                name = readNameFromField();  // Нужна логика на null moment
                if (name.equals("")){
                    Toast.makeText(saveWorkoutButton.getContext(), R.string.need_to_input_name, Toast.LENGTH_LONG).show();
                    return;
                }
                exercises = readExercisesFromFields();
            });
        }

        private String readNameFromField(){
            EditText inputedName = workoutBuilderAdapter.getItemViewByPosition(0).findViewById(R.id.inputedName);
            return inputedName.getText().toString();
        }

        private ArrayList<ArrayList<String>> readExercisesFromFields(){
            ArrayList<ArrayList<String>> exercises = new ArrayList<>();

            int length = workoutBuilderAdapter.getItemCount();
            if (length == 3){
                return null;
            }

            for (int i = 1; i < length - 2; i++){
                View item = workoutBuilderAdapter.getItemViewByPosition(i);

                ArrayList<String> exercise = new ArrayList<>();
                Spinner exerciseListSpinner = item.findViewById(R.id.exerciseList);
                exercise.add(exerciseListSpinner.getSelectedItem().toString());

                Spinner approachesListSpinner = item.findViewById(R.id.approachesList);
                exercise.add(approachesListSpinner.getSelectedItem().toString());

                Spinner repeatsListSpinner = item.findViewById(R.id.repeatsList);
                exercise.add(repeatsListSpinner.getSelectedItem().toString());

                exercises.add(exercise);
            }

            return exercises;
        }

        private void addItemViewToViewList(){
            workoutBuilderAdapter.addView(itemView);
        }
    }
}
