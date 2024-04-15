package com.example.samsungschoolproject.view_adapter.workout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.samsungschoolproject.R;
import com.example.samsungschoolproject.enums.WorkoutBuilderAdapterStates;
import com.example.samsungschoolproject.utils.ExerciseListUtils;
import com.example.samsungschoolproject.utils.WorkoutListUtils;
import com.example.samsungschoolproject.view_adapter.exercise.SpinnerAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WorkoutBuilderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private int length = 4;
    private WorkoutBuilderAdapterStates state;
    private EditText name;
    public ArrayList<ArrayList<String>> views;
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

    public void addView(ArrayList<String> template){
        if (state == WorkoutBuilderAdapterStates.ADAPTER_ON_CREATING){
            views.add(template);
        }
        if (state == WorkoutBuilderAdapterStates.ADAPTER_CREATED){
            views.add(length-3, template);
        }

    }

    public ArrayList<String> getItemViewByPosition(int position){
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
        workoutBuilderRecycler.removeViewAt(position);
        notifyItemRemoved(position);
    }

    public void setStateCreated(){
        state = WorkoutBuilderAdapterStates.ADAPTER_CREATED;
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
        private EditText name;
        private final WorkoutBuilderAdapter workoutBuilderAdapter;
        public InputNameViewHolder(@NonNull View itemView, WorkoutBuilderAdapter workoutBuilderAdapter) {
            super(itemView);
            this.itemView = itemView;
            this.workoutBuilderAdapter = workoutBuilderAdapter;

            workoutBuilderAdapter.addView(new ArrayList<>());
            initWidgets();
            workoutBuilderAdapter.name = itemView.findViewById(R.id.inputedName);
        }

        private void initWidgets(){
            name = itemView.findViewById(R.id.inputedName);
        }
    }

    public interface StartTemplateListFragment{
        public void startTemplateListFragment();
    }

    public static class ChooseExerciseViewHolder extends RecyclerView.ViewHolder{
        private final Button deleteExerciseButton;
        private final View itemView;
        private final Spinner exerciseListSpinner, approachesListSpinner, repeatsListSpinner;

        private final List<String> exercises;
        private final WorkoutBuilderAdapter workoutBuilderAdapter;

        public ChooseExerciseViewHolder(@NonNull View itemView, WorkoutBuilderAdapter workoutBuilderAdapter, List<String> exercises) {
            super(itemView);

            ArrayList<String> template = new ArrayList<>();
            template.add("Приседания");
            template.add("1");
            template.add("5");

            workoutBuilderAdapter.addView(template);

            this.itemView = itemView;
            this.exercises = exercises;
            this.workoutBuilderAdapter = workoutBuilderAdapter;

            deleteExerciseButton = itemView.findViewById(R.id.deleteExercise);
            exerciseListSpinner = itemView.findViewById(R.id.exerciseList);
            approachesListSpinner = itemView.findViewById(R.id.approachesList);
            repeatsListSpinner = itemView.findViewById(R.id.repeatsList);

            initButtonListeners();
            setSpinnerAdapters();
            setOnItemChangedListeners();
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

        private void setOnItemChangedListeners(){
            exerciseListSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    ArrayList<String> exercise = workoutBuilderAdapter.getItemViewByPosition(getBindingAdapterPosition());
                    if (exercise.isEmpty()){
                        exercise.add(parent.getItemAtPosition(position).toString());
                        exercise.add("1");
                        exercise.add("5");
                    }
                    else{
                        exercise.set(0, parent.getItemAtPosition(position).toString());
                    }

                    workoutBuilderAdapter.views.set(getBindingAdapterPosition(), exercise);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            approachesListSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    ArrayList<String> exercise = workoutBuilderAdapter.views.get(getBindingAdapterPosition());
                    exercise.set(1, parent.getItemAtPosition(position).toString());

                    workoutBuilderAdapter.views.set(getBindingAdapterPosition(), exercise);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            repeatsListSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    ArrayList<String> exercise = workoutBuilderAdapter.views.get(getBindingAdapterPosition());
                    exercise.set(2, parent.getItemAtPosition(position).toString());

                    workoutBuilderAdapter.views.set(getBindingAdapterPosition(), exercise);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
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
            workoutBuilderAdapter.addView(new ArrayList<>());

            initButtonListeners();
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

            workoutBuilderAdapter.addView(new ArrayList<>());

            initButtonListeners();
            workoutBuilderAdapter.setStateCreated();
        }

        private void initButtonListeners(){
            saveWorkoutButton.setOnClickListener( v -> {
                WorkoutListUtils.name = readNameFromField();
                if (WorkoutListUtils.name.isEmpty()){ // Отработает, если человек не ввел данные (в таком случае сохранить нельзя)
                    Toast.makeText(saveWorkoutButton.getContext(), R.string.need_to_input_name, Toast.LENGTH_LONG).show();
                    return;
                }
                WorkoutListUtils.exercises = readExercisesFromFields();

                startTemplatesListFragment(); // Запускает предыдущий фрагмент (список шаблонов)
            });
        }

        private String readNameFromField(){
            return workoutBuilderAdapter.name.getText().toString();
        }

        private ArrayList<ArrayList<String>> readExercisesFromFields(){
            ArrayList<ArrayList<String>> exercises = new ArrayList<>();

            int length = workoutBuilderAdapter.getItemCount();
            if (length == 3){
                return null;
            }

            for (int i = 1; i < length - 2; i++){
                ArrayList<String> exercise = workoutBuilderAdapter.views.get(i);
                exercises.add(exercise);
            }

            return exercises;
        }

        private void startTemplatesListFragment(){
            startTemplateListFragment.startTemplateListFragment();
        }
    }
}
