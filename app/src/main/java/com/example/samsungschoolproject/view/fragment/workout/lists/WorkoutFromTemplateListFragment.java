package com.example.samsungschoolproject.view.fragment.workout.lists;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.samsungschoolproject.R;
import com.example.samsungschoolproject.database.WorkoutHelperDatabase;
import com.example.samsungschoolproject.database.entity.PlannedWorkout;
import com.example.samsungschoolproject.database.entity.PlannedWorkoutExercise;
import com.example.samsungschoolproject.database.entity.WorkoutTemplate;
import com.example.samsungschoolproject.database.entity.WorkoutTemplateExercise;
import com.example.samsungschoolproject.model.repository.TemplateRepository;
import com.example.samsungschoolproject.model.repository.WorkoutRepository;
import com.example.samsungschoolproject.view.fragment.main.MainMenuInfoFragment;
import com.example.samsungschoolproject.model.util.CalendarUtils;
import com.example.samsungschoolproject.model.adapter.workout.list.TemplatesForWorkoutListAdapter;
import com.example.samsungschoolproject.model.adapter.workout.list.WorkoutListAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class WorkoutFromTemplateListFragment extends BottomSheetDialogFragment implements TemplatesForWorkoutListAdapter.OnTemplateForWorkoutItemListener {
    public static String TAG;
    private WorkoutRepository workoutRepository;
    private TemplateRepository templateRepository;
    private RecyclerView workoutTemplatesRecycler;
    private TextView headInfoTV;
    private TemplatesForWorkoutListAdapter templatesForWorkoutListAdapter;
    private final WorkoutListAdapter workoutListAdapter;
    private List<WorkoutTemplate> workoutTemplates;

    public WorkoutFromTemplateListFragment(WorkoutListAdapter workoutListAdapter){
        this.workoutListAdapter = workoutListAdapter;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_workout_from_template_choice, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        WorkoutHelperDatabase database = WorkoutHelperDatabase.getInstance(requireContext().getApplicationContext());
        workoutRepository = new WorkoutRepository(database);
        templateRepository = new TemplateRepository(database);

        initWidgets(view);
        loadTemplates();
    }

    // Необходим для того, чтобы обновить список отображаемых PlannedWorkout
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        workoutListAdapter.update();
    }

    private void initWidgets(View view){
        workoutTemplatesRecycler = view.findViewById(R.id.workoutTemplatesRecycler);
        headInfoTV = view.findViewById(R.id.headInfoTV);
    }

    // Загружает список из WorkoutTemplate's из БД
    private void loadTemplates(){
        workoutTemplates = templateRepository.getAllWorkoutTemplates();

        // Если не создано шаблонов, нужно изменить текст в TV
        if (workoutTemplates.isEmpty()){
            headInfoTV.setText(getResources().getString(R.string.no_templates_yet));
            return;
        }

        setWorkoutTemplatesRecycler();
    }

    private void setWorkoutTemplatesRecycler(){
        templatesForWorkoutListAdapter = new TemplatesForWorkoutListAdapter(workoutTemplates, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        workoutTemplatesRecycler.setLayoutManager(layoutManager);
        workoutTemplatesRecycler.setAdapter(templatesForWorkoutListAdapter);
    }

    // Отрабатывает при нажатии на WorkoutTemplate (т.е. при выборе шаблона, по которому нужно создать тренировку)
    @Override
    public void onWorkoutItemClick(int position) {
        // Показать AlertDialog (подтвердить создание workout на основе template)
        new AlertDialog.Builder(requireContext(), R.style.AlertTheme)
                .setTitle(R.string.planning_workout_from_template)
                .setMessage(getResources().getString(R.string.sure_use_this_template))
                .setPositiveButton(R.string.plan, (dialog, which) -> {
                    createWorkoutFromTemplate(position);
                    dialog.cancel();
                })
                .setNegativeButton(R.string.cancel, (dialog, which) -> {
                    dialog.cancel();
                })
                .show();
    }

    private void createWorkoutFromTemplate(int position){
        WorkoutTemplate workoutTemplate = workoutTemplates.get(position);

        if (workoutRepository.createNewPlannedWorkoutFromTemplate(workoutTemplate)){
            // Обновить статистику
            MainMenuInfoFragment.loadStatisticsData(requireContext().getApplicationContext(),
                    getResources().getString(R.string.workouts_count),
                    getResources().getString(R.string.completed_workouts_count),
                    getResources().getString(R.string.completed_workouts_length),
                    getResources().getString(R.string.the_most_preferred_exercise));

            // Отобразить Toast о том, что тренировка создана
            Toast.makeText(requireContext().getApplicationContext(), R.string.workout_planned, Toast.LENGTH_SHORT).show();

            // Закрыть текущий фрагмент после выбора
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction().remove(this).commit();
        }
        else Toast.makeText(getContext(), R.string.already_planned_today, Toast.LENGTH_SHORT).show();
    }
}

