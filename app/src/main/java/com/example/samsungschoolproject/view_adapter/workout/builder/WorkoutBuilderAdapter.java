package com.example.samsungschoolproject.view_adapter.workout.builder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.samsungschoolproject.R;
import com.example.samsungschoolproject.enums.WorkoutBuilderAdapterStates;
import com.example.samsungschoolproject.utils.ExerciseListUtils;
import com.example.samsungschoolproject.view_adapter.exercise.SpinnerAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WorkoutBuilderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private int length;
    private WorkoutBuilderAdapterStates adapterCreatingState;
    private EditText name;
    private ArrayList<ArrayList<String>> viewItems;
    private final RecyclerView workoutBuilderRecycler;
    private final LoadJustCreated loadJustCreated;
    private final List<String> exercises;

    public WorkoutBuilderAdapter (
            List<String> exercises,
            RecyclerView workoutBuilderRecycler,
            LoadJustCreated loadJustCreated){
        this.workoutBuilderRecycler = workoutBuilderRecycler;
        this.exercises = exercises;
        this.adapterCreatingState = WorkoutBuilderAdapterStates.ADAPTER_ON_CREATING;
        this.loadJustCreated = loadJustCreated;

        // Инициализация полей необходимыми значениями
        viewItems = new ArrayList<>();
        length = 4; // т.к. стартовая длина равна четырем
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
                return new SaveWorkoutButtonViewHolder(view, this, loadJustCreated);
        }

        return null;
    }

    // Возвращает необходимый "тип" view в зависимости от позиции
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
    public ArrayList<String> getItemViewByPosition(int position){
        return viewItems.get(position);
    }

    // Добавляет Exercise в отображение
    public void addExercise(){
        length++;
        notifyItemChanged(length-3);
        notifyItemChanged(length-2);
        notifyItemChanged(length-1);

        ArrayList<String> template = new ArrayList<>();
        template.add("Приседания");
        template.add("1");
        template.add("5");

        // Добавить новый элемент в массив viewItems
        addToViewItems(template);
    }

    // Метод добавляет новый элемент в массив viewItems, содержащий информацию о view
    public void addToViewItems(ArrayList<String> template){
        // Если адаптер на стадии создания, то вьюшка добавляется в конец
        if (adapterCreatingState == WorkoutBuilderAdapterStates.ADAPTER_ON_CREATING){
            viewItems.add(template);
        }
        // Если адаптер уже создан, то это создание Exercise и оно добавляется перед кнопками
        if (adapterCreatingState == WorkoutBuilderAdapterStates.ADAPTER_CREATED && !template.isEmpty()){
            viewItems.add(length-3, template);
        }
    }

    // Удаляет Exercise из отображения и из массива viewItems
    public void deleteExercise(int position){
        length--;
        viewItems.remove(position);
        notifyItemRemoved(position);
    }

    // Возвращает текущее состояние адаптера
    public WorkoutBuilderAdapterStates getState(){
        return adapterCreatingState;
    }

    // Устанавливает состояние о том, что адаптер был полностью создан
    public void setStateCreated(){
        adapterCreatingState = WorkoutBuilderAdapterStates.ADAPTER_CREATED;
    }

    public void setName(EditText newName){
        name = newName;
    }

    public String getName(){
        return name.getText().toString();
    }

    public void UpdateViewItem(int position, ArrayList<String> item){
        viewItems.set(position, item);
    }

    public ArrayList<String> getFromViewItems(int position){
        return viewItems.get(position);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return length;
    }

    public interface LoadJustCreated {
        void loadJustCreated(String name, ArrayList<ArrayList<String>> exercises);
    }

    public static class InputNameViewHolder extends RecyclerView.ViewHolder{
        public InputNameViewHolder(@NonNull View itemView, WorkoutBuilderAdapter workoutBuilderAdapter) {
            super(itemView);

            workoutBuilderAdapter.addToViewItems(new ArrayList<>());
            workoutBuilderAdapter.setName(itemView.findViewById(R.id.inputedName));
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

            // Необходимо предзаполнить массив значениями "упражнения" по умолчанию
            ArrayList<String> template = new ArrayList<>();
            template.add("Приседания");
            template.add("1");
            template.add("5");

            // Только если адаптер на стадии создания, то этот объект в список viewItems добавляется напрямую из конструктора holder'a
            if (workoutBuilderAdapter.getState().equals(WorkoutBuilderAdapterStates.ADAPTER_ON_CREATING)) workoutBuilderAdapter.addToViewItems(template);

            this.itemView = itemView;
            this.exercises = exercises;
            this.workoutBuilderAdapter = workoutBuilderAdapter;

            deleteExerciseButton = itemView.findViewById(R.id.delete);
            exerciseListSpinner = itemView.findViewById(R.id.exerciseList);
            approachesListSpinner = itemView.findViewById(R.id.approachesList);
            repeatsListSpinner = itemView.findViewById(R.id.repeatsList);

            initButtonListeners();
            setSpinnerAdapters();
            setOnItemChangedListeners();
        }

        private void initButtonListeners(){
            deleteExerciseButton.setOnClickListener(v -> workoutBuilderAdapter.deleteExercise(getBindingAdapterPosition()));
        }

        // Метод устанавливает содержимое для спиннеров
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

        // Метод устанавливает Listeners на изменение значения в спиннере, чтобы отслеживать выбранное упражнение
        private void setOnItemChangedListeners(){
            exerciseListSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    ArrayList<String> exercise = workoutBuilderAdapter.getItemViewByPosition(getBindingAdapterPosition());
                    /* При добавлении "второго" упражнения во время создания тренировки/шаблона
                     * зачем-то создается 3 объекта, причем второй и третий пустые. Потому приходится
                     * проверять, что exercise для этого объекта не пустой и объект был создан корректно */ // TODO: Выяснить, почему создается 3 объекта вместо 1
                    if (exercise.isEmpty()){
                        exercise.add(parent.getItemAtPosition(position).toString());
                        exercise.add("1");
                        exercise.add("5");
                    }
                    else{
                        exercise.set(0, parent.getItemAtPosition(position).toString());
                    }

                    workoutBuilderAdapter.UpdateViewItem(getBindingAdapterPosition(), exercise);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {} // Нет логики
            });

            approachesListSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    ArrayList<String> exercise = workoutBuilderAdapter.getFromViewItems(getBindingAdapterPosition());
                    exercise.set(1, parent.getItemAtPosition(position).toString());

                    workoutBuilderAdapter.UpdateViewItem(getBindingAdapterPosition(), exercise);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {} // Нет логики
            });

            repeatsListSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    ArrayList<String> exercise = workoutBuilderAdapter.getFromViewItems(getBindingAdapterPosition());
                    exercise.set(2, parent.getItemAtPosition(position).toString());

                    workoutBuilderAdapter.UpdateViewItem(getBindingAdapterPosition(), exercise);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {} // Нет логики
            });
        }
    }

    public static class AddExerciseViewHolder extends RecyclerView.ViewHolder{
        private final ImageButton addExerciseButton;
        private final WorkoutBuilderAdapter workoutBuilderAdapter;

        public AddExerciseViewHolder(@NonNull View itemView, WorkoutBuilderAdapter workoutBuilderAdapter) {
            super(itemView);
            addExerciseButton = itemView.findViewById(R.id.addExercise);
            this.workoutBuilderAdapter = workoutBuilderAdapter;
            workoutBuilderAdapter.addToViewItems(new ArrayList<>());

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
        private final WorkoutBuilderAdapter workoutBuilderAdapter;
        private final LoadJustCreated loadJustCreated;

        public SaveWorkoutButtonViewHolder(
                @NonNull View itemView,
                WorkoutBuilderAdapter workoutBuilderAdapter,
                LoadJustCreated loadJustCreated) {
            super(itemView);
            saveWorkoutButton = itemView.findViewById(R.id.saveWorkout);
            this.workoutBuilderAdapter = workoutBuilderAdapter;
            this.loadJustCreated = loadJustCreated;

            workoutBuilderAdapter.addToViewItems(new ArrayList<>());

            initButtonListeners();
            workoutBuilderAdapter.setStateCreated(); // Устанавливает состояние, что adapter создан
        }

        private void initButtonListeners(){
            saveWorkoutButton.setOnClickListener( v -> {
                String name = readNameFromField();
                if (name.isEmpty()){ // Отработает, если человек не ввел данные (в таком случае сохранить нельзя)
                    Toast.makeText(saveWorkoutButton.getContext(), R.string.need_to_input_name, Toast.LENGTH_LONG).show();
                    return;
                }
                ArrayList<ArrayList<String>> exercises = readExercisesFromFields();

                // Загружает в БД созданную нажатием кнопки тренировки. Реализация вынесена во фрагмент
                loadJustCreated.loadJustCreated(name, exercises);
            });
        }

        // Возвращает введенное пользователем название тренировки
        private String readNameFromField(){
            return workoutBuilderAdapter.getName();
        }

        // Возвращает выбранные пользователем упражнения
        private ArrayList<ArrayList<String>> readExercisesFromFields(){
            ArrayList<ArrayList<String>> exercises = new ArrayList<>();

            // Если количество объектов равно 3, то в тренировке нет упражнений
            int length = workoutBuilderAdapter.getItemCount();
            if (length == 3){
                return null;
            }

            // Считывает данные только с "упражнений"
            for (int i = 1; i < length - 2; i++){
                ArrayList<String> exercise = workoutBuilderAdapter.getFromViewItems(i);
                exercises.add(exercise);
            }

            return exercises;
        }
    }
}
