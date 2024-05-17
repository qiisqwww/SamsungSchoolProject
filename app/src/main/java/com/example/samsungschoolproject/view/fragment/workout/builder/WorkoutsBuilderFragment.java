package com.example.samsungschoolproject.view.fragment.workout.builder;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.example.samsungschoolproject.database.entity.PlannedWorkout;
import com.example.samsungschoolproject.database.entity.PlannedWorkoutExercise;
import com.example.samsungschoolproject.model.enums.BackFragmentForBuilderStates;
import com.example.samsungschoolproject.model.repository.ExerciseRepository;
import com.example.samsungschoolproject.model.repository.WorkoutRepository;
import com.example.samsungschoolproject.view.fragment.main.MainMenuInfoFragment;
import com.example.samsungschoolproject.view.fragment.сalendar.MonthCalendarFragment;
import com.example.samsungschoolproject.model.util.CalendarUtils;
import com.example.samsungschoolproject.model.util.ExerciseListUtils;
import com.example.samsungschoolproject.model.util.WorkoutListUtils;
import com.example.samsungschoolproject.model.adapter.workout.builder.WorkoutBuilderAdapter;
import com.example.samsungschoolproject.model.adapter.workout.list.WorkoutListAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class WorkoutsBuilderFragment extends BottomSheetDialogFragment implements WorkoutBuilderAdapter.LoadJustCreated {
    public static String TAG;
    private ImageButton goBackButton;
    private BackFragmentForBuilderStates backFragmentForBuilderStates;
    private WorkoutListAdapter workoutListAdapter;
    private WorkoutRepository workoutRepository;
    private ExerciseRepository exerciseRepository;
    private RecyclerView workoutBuilderRecycler;

    public WorkoutsBuilderFragment(BackFragmentForBuilderStates backFragmentForBuilderStates, WorkoutListAdapter workoutListAdapter){
        this.backFragmentForBuilderStates = backFragmentForBuilderStates;
        this.workoutListAdapter = workoutListAdapter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_workouts_builder, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        WorkoutHelperDatabase database = WorkoutHelperDatabase.getInstance(requireContext().getApplicationContext());
        workoutRepository = new WorkoutRepository(database);
        exerciseRepository = new ExerciseRepository(database);

        initWidgets(view);
        initButtonListeners();

        setWorkoutBuilderRecycler();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        workoutListAdapter.update();
    }

    private void initWidgets(View view){
        goBackButton = view.findViewById(R.id.back);

        workoutBuilderRecycler = view.findViewById(R.id.workoutBuilderRecycler);
    }

    private void initButtonListeners(){
        goBackButton.setOnClickListener(v -> startPreviousFragment());
    }

    private void setWorkoutBuilderRecycler(){
       List<String> stringExercises = ExerciseListUtils.parseExerciseToStrings(exerciseRepository.getAllExercises());

        WorkoutBuilderAdapter workoutBuilderAdapter = new WorkoutBuilderAdapter(stringExercises, workoutBuilderRecycler, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        workoutBuilderRecycler.setLayoutManager(layoutManager);
        workoutBuilderRecycler.setAdapter(workoutBuilderAdapter);
    }

    // Загружает в БД только что созданную тренировку
    @Override
    public void loadJustCreated(String name, ArrayList<ArrayList<String>> unparsedExercises) {
        PlannedWorkout plannedWorkout = new PlannedWorkout(name, WorkoutListUtils.countWorkoutLength(unparsedExercises), "false", CalendarUtils.selectedDate.toString());
        if (workoutRepository.loadNewPlannedWorkout(plannedWorkout, unparsedExercises)){
            // Обновить статистику
            MainMenuInfoFragment.loadStatisticsData(requireContext().getApplicationContext(),
                    getResources().getString(R.string.workouts_count),
                    getResources().getString(R.string.completed_workouts_count),
                    getResources().getString(R.string.completed_workouts_length),
                    getResources().getString(R.string.the_most_preferred_exercise));

            // Отобразить Toast о том, что тренировка создана
            Toast.makeText(requireContext().getApplicationContext(), R.string.workout_planned, Toast.LENGTH_SHORT).show();

            // Вернуться в предыдущий фрагмент
            startPreviousFragment();
        }
        else Toast.makeText(requireContext().getApplicationContext(),  R.string.workout_already_exists, Toast.LENGTH_SHORT).show();
    }

    // Запускает предыдущий фрагмент исходя из состояния BackFragmentForBuilderStates
    public void startPreviousFragment() {
        if (backFragmentForBuilderStates == BackFragmentForBuilderStates.BACK_TO_WEEK_FRAGMENT){
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction().remove(this).commit();
        }
        if (backFragmentForBuilderStates == BackFragmentForBuilderStates.BACK_TO_MONTH_FRAGMENT){
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction().remove(this).commit();
            MonthCalendarFragment.ModalBottomSheetFragment modalBottomSheet = new MonthCalendarFragment.ModalBottomSheetFragment(workoutRepository);
            MonthCalendarFragment.ModalBottomSheetFragment.TAG = "Another Instance"; // idk if this name is important

            modalBottomSheet.show(getActivity().getSupportFragmentManager(), MonthCalendarFragment.ModalBottomSheetFragment.TAG);
        }
    }


}