package com.example.samsungschoolproject.fragment.workout;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.samsungschoolproject.R;
import com.example.samsungschoolproject.database.WorkoutHelperDatabase;
import com.example.samsungschoolproject.database.model.Exercise;
import com.example.samsungschoolproject.database.model.WorkoutTemplate;
import com.example.samsungschoolproject.database.model.WorkoutTemplateExercise;
import com.example.samsungschoolproject.utils.ExerciseListUtils;
import com.example.samsungschoolproject.utils.WorkoutListUtils;
import com.example.samsungschoolproject.view_adapter.workout.WorkoutBuilderAdapter;

import java.util.ArrayList;
import java.util.List;


public class TemplatesBuilderFragment extends Fragment implements WorkoutBuilderAdapter.StartPreviousFragment, WorkoutBuilderAdapter.LoadJustCreated {
    private Button goBackButton;
    private WorkoutHelperDatabase database;
    private RecyclerView templateBuilderRecycler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_templates_builder, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        database = WorkoutHelperDatabase.getInstance(requireContext().getApplicationContext()); //  Получить объект БД

        initWidgets(view);
        initButtonListeners();

        setWorkoutBuilderRecycler();
    }

    private void initWidgets(View view){
        goBackButton = view.findViewById(R.id.goBack);

        templateBuilderRecycler = view.findViewById(R.id.templateBuilderRecycler);
    }

    private void initButtonListeners(){
        goBackButton.setOnClickListener(v -> {
            TemplatesListFragment templatesListFragment = new TemplatesListFragment();
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.popBackStack();
            fragmentManager.beginTransaction()
                    .replace(R.id.workoutTemplatesContainer, templatesListFragment)
                    .commit();
        });
    }

    private List<Exercise> getAllExercises(){
        List<Exercise> exercises = database.getExerciseDAO().getAllExercises();
        return exercises;
    }

    private void setWorkoutBuilderRecycler(){
        List<String> stringExercises = ExerciseListUtils.parseExerciseToStrings(getAllExercises());

        WorkoutBuilderAdapter workoutBuilderAdapter = new WorkoutBuilderAdapter(stringExercises, templateBuilderRecycler, this, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        templateBuilderRecycler.setLayoutManager(layoutManager);
        templateBuilderRecycler.setAdapter(workoutBuilderAdapter);
    }

    @Override
    public void loadJustCreated(String name, ArrayList<ArrayList<String>> exercises) {
        WorkoutTemplate workoutTemplate = new WorkoutTemplate(name, WorkoutListUtils.countWorkoutLength(exercises));
        database.getWorkoutTemplateDAO().addWorkoutTemplate(workoutTemplate);

        workoutTemplate = database.getWorkoutTemplateDAO().getWorkoutTemplateByName(name);
        for (int i = 0; i < exercises.size(); i++){
            ArrayList<String> exerciseInfo = exercises.get(i);
            Exercise exercise = database.getExerciseDAO().getExerciseByName(exerciseInfo.get(0));
            database.getWorkoutTemplateExerciseDAO().addWorkoutTemplateExercise(new WorkoutTemplateExercise(
                    workoutTemplate.id,
                    exercise.id,
                    Integer.valueOf(exerciseInfo.get(2)),
                    Integer.valueOf(exerciseInfo.get(1)),
                    i+1
            ));
        }
    }

    @Override
    public void startPreviousFragment() {
        TemplatesListFragment templatesListFragment = new TemplatesListFragment();
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.remove(new TemplatesListFragment());
        fragmentTransaction.replace(R.id.workoutTemplatesContainer, templatesListFragment).commit();
    }
}