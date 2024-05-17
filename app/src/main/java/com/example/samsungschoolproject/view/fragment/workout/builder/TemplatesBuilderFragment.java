package com.example.samsungschoolproject.view.fragment.workout.builder;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.samsungschoolproject.R;
import com.example.samsungschoolproject.database.WorkoutHelperDatabase;
import com.example.samsungschoolproject.database.entity.Exercise;
import com.example.samsungschoolproject.database.entity.WorkoutTemplate;
import com.example.samsungschoolproject.database.entity.WorkoutTemplateExercise;
import com.example.samsungschoolproject.model.repository.ExerciseRepository;
import com.example.samsungschoolproject.model.repository.TemplateRepository;
import com.example.samsungschoolproject.view.fragment.workout.lists.TemplatesListFragment;
import com.example.samsungschoolproject.model.util.ExerciseListUtils;
import com.example.samsungschoolproject.model.util.WorkoutListUtils;
import com.example.samsungschoolproject.model.adapter.workout.builder.WorkoutBuilderAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


public class TemplatesBuilderFragment extends Fragment implements WorkoutBuilderAdapter.LoadJustCreated {
    private ImageButton goBackButton;
    private TemplateRepository templateRepository;
    private ExerciseRepository exerciseRepository;
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

        WorkoutHelperDatabase database = WorkoutHelperDatabase.getInstance(requireContext().getApplicationContext()); //  Получить объект БД
        templateRepository = new TemplateRepository(database);
        exerciseRepository = new ExerciseRepository(database);

        initWidgets(view);
        initButtonListeners();
        setTemplateBuilderRecycler();
    }

    private void initWidgets(View view){
        goBackButton = view.findViewById(R.id.back);

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

    private void setTemplateBuilderRecycler(){
        List<String> stringExercises = ExerciseListUtils.parseExerciseToStrings(exerciseRepository.getAllExercises());

        WorkoutBuilderAdapter workoutBuilderAdapter = new WorkoutBuilderAdapter(stringExercises, templateBuilderRecycler, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        templateBuilderRecycler.setLayoutManager(layoutManager);
        templateBuilderRecycler.setAdapter(workoutBuilderAdapter);
    }

    // Загружает в БД только что созданную тренировку
    @Override
    public void loadJustCreated(String name, ArrayList<ArrayList<String>> unparsedExercises) {
        WorkoutTemplate workoutTemplate = new WorkoutTemplate(name, WorkoutListUtils.countWorkoutLength(unparsedExercises));

        if (templateRepository.loadNewTemplate(workoutTemplate, unparsedExercises)) {
            // Отобразить Toast о том, что шаблон создан
            Toast.makeText(requireContext().getApplicationContext(), R.string.template_created, Toast.LENGTH_SHORT).show();

            // Запустить предыдущий фрагмент
            startPreviousFragment();
        }
        else{
            Toast.makeText(requireContext().getApplicationContext(), R.string.template_already_exists, Toast.LENGTH_SHORT).show();
        }
    }

    // Запускает предыдущий фрагмент
    public void startPreviousFragment() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.popBackStack();
        fragmentManager.beginTransaction().replace(R.id.workoutTemplatesContainer, new TemplatesListFragment()).commit();
    }
}