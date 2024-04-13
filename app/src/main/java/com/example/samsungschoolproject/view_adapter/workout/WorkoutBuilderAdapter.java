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
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.samsungschoolproject.R;
import com.example.samsungschoolproject.database.model.Workout;
import com.example.samsungschoolproject.enums.WorkoutBuilderAdapterStates;
import com.example.samsungschoolproject.fragment.workout.TemplatesBuilderFragment;
import com.example.samsungschoolproject.fragment.workout.TemplatesListFragment;
import com.example.samsungschoolproject.utils.ExerciseListUtils;
import com.example.samsungschoolproject.utils.WorkoutListUtils;
import com.example.samsungschoolproject.view_adapter.exercise.SpinnerAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WorkoutBuilderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private int length = 0;
    private WorkoutBuilderAdapterStates state;

    private ArrayList<View> views;
    private final RecyclerView workoutBuilderRecycler;
    private final StartTemplateListFragment startTemplateListFragment;
    private final List<String> exercises;

    public WorkoutBuilderAdapter (List<String> exercises, RecyclerView workoutBuilderRecycler, StartTemplateListFragment startTemplateListFragment){
        this.workoutBuilderRecycler = workoutBuilderRecycler;
        this.exercises = exercises;
        this.startTemplateListFragment = startTemplateListFragment;
        this.state = WorkoutBuilderAdapterStates.ADAPTER_ON_CREATING;

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
                return new SaveWorkoutButtonViewHolder(view, this, startTemplateListFragment);
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
        if (state==WorkoutBuilderAdapterStates.ADAPTER_ON_CREATING){
            views.add(view);
            length++;
        }
        if(state==WorkoutBuilderAdapterStates.ADAPTER_CREATED){
            views.add(length, view); // Не нужно увеличивать length, т.к. увеличится при вызове addExercise
        }
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

    public void setStateCreated(){
        state = WorkoutBuilderAdapterStates.ADAPTER_CREATED;
    }

    public WorkoutBuilderAdapterStates getState(){
        return state;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return length;
    }

    public interface StartTemplateListFragment{
        public void startTemplateListFragment();
    }

    public static class InputNameViewHolder extends RecyclerView.ViewHolder{
        private final View itemView;
        private final WorkoutBuilderAdapter workoutBuilderAdapter;
        public InputNameViewHolder(@NonNull View itemView, WorkoutBuilderAdapter workoutBuilderAdapter) {
            super(itemView);
            this.itemView = itemView;
            this.workoutBuilderAdapter = workoutBuilderAdapter;

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

            initButtonListeners();
            setSpinnerAdapters();

            if (workoutBuilderAdapter.getState() == WorkoutBuilderAdapterStates.ADAPTER_ON_CREATING){
                workoutBuilderAdapter.addView(itemView);
                return;
            }

            workoutBuilderAdapter.addView(itemView);
        }

        private void initButtonListeners(){
            deleteExerciseButton.setOnClickListener(v -> {
                workoutBuilderAdapter.deleteExercise(getBindingAdapterPosition());
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

            workoutBuilderAdapter.addView(itemView);
        }

        private void initButtonListeners(){
            addExerciseButton.setOnClickListener(v -> {
                workoutBuilderAdapter.addExercise();
            });
        }
    }

    public static class SaveWorkoutButtonViewHolder extends RecyclerView.ViewHolder{
        private final Button saveWorkoutButton;
        private final View itemView;
        private final WorkoutBuilderAdapter workoutBuilderAdapter;
        private final StartTemplateListFragment startTemplateListFragment;

        public SaveWorkoutButtonViewHolder(@NonNull View itemView, WorkoutBuilderAdapter workoutBuilderAdapter, StartTemplateListFragment startTemplateListFragment) {
            super(itemView);
            this.itemView = itemView;

            saveWorkoutButton = itemView.findViewById(R.id.saveWorkout);
            this.workoutBuilderAdapter = workoutBuilderAdapter;
            this.startTemplateListFragment = startTemplateListFragment;

            initButtonListeners();
            workoutBuilderAdapter.addView(itemView);
            workoutBuilderAdapter.setStateCreated();
        }

        private void initButtonListeners(){
            saveWorkoutButton.setOnClickListener( v -> {
                WorkoutListUtils.name = readNameFromField();
                if (WorkoutListUtils.name.equals("")){ // Отработает, если человек не ввел данные (в таком случае сохранить нельзя)
                    Toast.makeText(saveWorkoutButton.getContext(), R.string.need_to_input_name, Toast.LENGTH_LONG).show();
                    return;
                }
                WorkoutListUtils.exercises = readExercisesFromFields();

                startTemplatesListFragment(); // Запускает предыдущий фрагмент (список шаблонов)
            });
        }

        private String readNameFromField(){
            EditText inputtedName = workoutBuilderAdapter.getItemViewByPosition(0).findViewById(R.id.inputedName);
            return inputtedName.getText().toString();
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

        private void startTemplatesListFragment(){
            startTemplateListFragment.startTemplateListFragment();
        }
    }
}
